package com.wallace.githubtoprepositories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wallace.githubtoprepositories.db.RepositoryDatabase
import com.wallace.githubtoprepositories.paging.RepositoryRemoteMediator
import com.wallace.githubtoprepositories.remote.GithubApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    githubApiService: GithubApiService,
    repositoryDatabase: RepositoryDatabase
) :
    ViewModel() {

        @OptIn(ExperimentalPagingApi::class)
        val listOfRepository = Pager(
            config = PagingConfig(
                pageSize = 50,
            ),
            remoteMediator = RepositoryRemoteMediator(
                query = "stars",
                database = repositoryDatabase,
                service = githubApiService,
            ),
            pagingSourceFactory = {
                repositoryDatabase.getRepositoryDao().getAll()
            }
        ).flow.cachedIn(viewModelScope)

}