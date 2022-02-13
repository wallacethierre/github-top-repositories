package com.wallace.githubtoprepositories.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wallace.githubtoprepositories.db.PageIndexEntry
import com.wallace.githubtoprepositories.db.RepositoryDatabase
import com.wallace.githubtoprepositories.db.RepositoryEntry
import com.wallace.githubtoprepositories.remote.GithubApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RepositoryRemoteMediator(
    private val query: String,
    private val database: RepositoryDatabase,
    private val service: GithubApiService

) : RemoteMediator<Int, RepositoryEntry>() {
    val repositoryDao = database.getRepositoryDao()
    val pageIndexDao = database.getPageIndexDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntry>
    ): MediatorResult {

        val page = when (loadType) {
            // The network load method takes an optional String
            // parameter. For every page after the first, pass the String
            // token returned from the previous page to let it continue
            // from where it left off. For REFRESH, pass null to load the
            // first page.
            LoadType.REFRESH -> null
            // In this example, you never need to prepend, since REFRESH
            // will always load the first page in the list. Immediately
            // return, reporting end of pagination.
            LoadType.PREPEND -> return MediatorResult.Success(
                endOfPaginationReached = true
            )
            LoadType.APPEND -> {
                val remoteKey = pageIndexDao.searchRemoteKeyById(query)

                // You must explicitly check if the page key is null when
                // appending, since null is only valid for initial load.
                // If you receive null for APPEND, that means you have
                // reached the end of pagination and there are no more
                // items to load.
                if (remoteKey?.nextPage == null) {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                remoteKey.nextPage
            }
        }
        return getResult(page, state, loadType)
    }

    private suspend fun getResult(
        pages: Int?,
        state: PagingState<Int, RepositoryEntry>,
        loadType: LoadType
    ): MediatorResult {
        try {
            val page = pages ?: 1
            val apiResponse = service.getAllRepositories(
                "stars",
                page ,
                state.config.pageSize
            )
            val repos = apiResponse.repositoryResponseList
            val endOfPaginationReached = repos.isEmpty()
            if (endOfPaginationReached.not()) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        repositoryDao.deleteAll()
                        pageIndexDao.deleteByKey(query)
                    }

                    val nextPage = page+1
                    pageIndexDao.insert(
                        PageIndexEntry(id = query, nextPage = nextPage)
                    )
                    val rpoEntry = repos.map {
                    RepositoryEntry(
                        it.id.toLong(),
                        it.name,
                        it.numberOfStars,
                        it.numberOfForks,
                        it.owner.login,
                        it.owner.avatarURL
                    )
                }
                repositoryDao.insertAll(rpoEntry)
                }
                MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


//        val pageKeyData = getKeyPageData(loadType, state)
//        val page = when (pageKeyData) {
//            is MediatorResult.Success -> {
//                return pageKeyData
//            }
//            else -> {
//                pageKeyData as Int
//            }
//        }
//
//        try {
//            val response = service.getAllRepositories(query, page, state.config.pageSize)
//            val isEndOfList = response.githubItemResponseList.isEmpty()
//            database.withTransaction {
//                // clear all tables in the database
//                if (loadType == LoadType.REFRESH) {
//                    pageIndexDao.deleteAll()
//                    repositoryDao.deleteAll()
//                }
//                val prevKey = if (page == 1) null else page - 1
//                val nextKey = if (isEndOfList) null else page + 1
//                val keys = response.githubItemResponseList.map { gitRepoItem ->
//                    PageIndexEntry(id = gitRepoItem.id.toLong(), prevPage = prevKey, nextPage = nextKey)
//                }
//                pageIndexDao.insertAll(keys)
//                val rpoEntry = response.githubItemResponseList.map {
//                    RepositoryEntry(
//                        it.id.toLong(),
//                        it.name,
//                        it.numberOfStars,
//                        it.numberOfForks,
//                        it.owner.login,
//                        it.owner.avatarURL
//                    )
//                }
//                repositoryDao.insertAll(rpoEntry)
//            }
//            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
//        } catch (exception: IOException) {
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            return MediatorResult.Error(exception)
//        }
    }



//
//    /**
//     * this returns the page key or the final end of list success result
//     */
//    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, RepositoryEntry>): Any? {
//        return when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getClosestRemoteKey(state)
//                remoteKeys?.nextPage?.minus(1) ?: 1
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getLastRemoteKey(state)
//                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
//                remoteKeys.nextPage
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getFirstRemoteKey(state)
//                    ?: throw InvalidObjectException("Invalid state, key should not be null")
//                //end of list condition reached
//                remoteKeys.prevPage ?: return MediatorResult.Success(endOfPaginationReached = true)
//                remoteKeys.prevPage
//            }
//        }
//    }
//
//    /**
//     * get the last remote key inserted which had the data
//     */
//    private suspend fun getLastRemoteKey(state: PagingState<Int, RepositoryEntry>): PageIndexEntry? {
//        return state.pages
//            .lastOrNull { it.data.isNotEmpty() }
//            ?.data?.lastOrNull()
//            ?.let { repositoryEntry -> pageIndexDao.searchRemoteKeyById(repositoryEntry.id) }
//    }
//
//    /**
//     * get the first remote key inserted which had the data
//     */
//    private suspend fun getFirstRemoteKey(state: PagingState<Int, RepositoryEntry>): PageIndexEntry? {
//        return state.pages
//            .firstOrNull() { it.data.isNotEmpty() }
//            ?.data?.firstOrNull()
//            ?.let { repositoryEntry -> pageIndexDao.searchRemoteKeyById(repositoryEntry.id) }
//    }
//
//    /**
//     * get the closest remote key inserted which had the data
//     */
//    private suspend fun getClosestRemoteKey(state: PagingState<Int, RepositoryEntry>): PageIndexEntry? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { repositoryEntryId ->
//                pageIndexDao.searchRemoteKeyById(repositoryEntryId)
//            }
//        }
//    }
//
//
//}
