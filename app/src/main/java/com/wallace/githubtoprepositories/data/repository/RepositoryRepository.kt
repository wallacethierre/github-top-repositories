package com.wallace.githubtoprepositories.data.repository

import com.wallace.githubtoprepositories.data.remote.GithubApiService
import javax.inject.Inject

class RepositoryRepository @Inject constructor(private val githubApiService: GithubApiService) {
    suspend fun getAllGithubRepositories(sort: String, page: Int, perPage: Int) =
        githubApiService.getAllRepositories(sort, page, perPage)
}