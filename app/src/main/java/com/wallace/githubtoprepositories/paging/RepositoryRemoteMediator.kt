package com.wallace.githubtoprepositories.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wallace.githubtoprepositories.data.db.PageIndexEntry
import com.wallace.githubtoprepositories.data.db.RepositoryDatabase
import com.wallace.githubtoprepositories.data.db.RepositoryEntry
import com.wallace.githubtoprepositories.data.remote.GithubApiService
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
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(
                endOfPaginationReached = true
            )
            LoadType.APPEND -> {
                val remoteKey = pageIndexDao.searchRemoteKeyById(query)

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
                page,
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

                    val nextPage = page + 1
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
                    endOfPaginationReached = endOfPaginationReached
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}

