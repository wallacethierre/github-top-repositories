package com.wallace.githubtoprepositories.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wallace.githubtoprepositories.model.RepositoryResponse
import com.wallace.githubtoprepositories.repository.RepositoryRepository

class RepositoryPagingSource(
    private val repository: RepositoryRepository,
    private val sort: String
) : PagingSource<Int, RepositoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, RepositoryResponse>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryResponse> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getAllGithubRepositories(sort, currentPage, 50)
            val data = response?.repositoryResponseList ?: emptyList()
            val responseData = mutableListOf<RepositoryResponse>()
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