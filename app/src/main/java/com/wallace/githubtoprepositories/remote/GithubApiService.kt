package com.wallace.githubtoprepositories.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories?q=language:kotlin")
    suspend fun getAllRepositories(
        @Query("sort") sortBy: String,
        @Query("page") pageNumber: Int
    ): Response<RepositoryResponse>

}