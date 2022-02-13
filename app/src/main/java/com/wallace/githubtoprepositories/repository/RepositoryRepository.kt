package com.wallace.githubtoprepositories.repository

import com.wallace.githubtoprepositories.remote.GithubApiService
import javax.inject.Inject

class RepositoryRepository @Inject constructor(private val githubApiService: GithubApiService) {
    suspend fun getAllGithubRepositories(sort: String, page: Int, perPage: Int) =
        githubApiService.getAllRepositories(sort, page, perPage)
}