package com.wallace.githubtoprepositories.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories?q=language:kotlin")
    suspend fun getAllRepositories(
        @Query("sort") sortBy: String? = "stars",
        @Query("page") pageNumber: Int?,
        @Query("per_page") perPage: Int?,
    ): GithubResponse

}