package com.wallace.githubtoprepositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wallace.githubtoprepositories.model.Repository
import com.wallace.githubtoprepositories.repository.GithubRepRepository

class RepositoryPagingSource(
    private val repository: GithubRepRepository,
    private val sort: String
) : PagingSource<Int, Repository>() {
    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getAllGithubRepositories(sort, currentPage)
            val data = response.body()?.repositoryList ?: emptyList()
            val responseData = mutableListOf<Repository>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}