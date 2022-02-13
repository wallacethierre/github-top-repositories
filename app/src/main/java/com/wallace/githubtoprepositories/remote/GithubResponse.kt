package com.wallace.githubtoprepositories.remote

import com.google.gson.annotations.SerializedName
import com.wallace.githubtoprepositories.model.RepositoryResponse

data class GithubResponse (
    @SerializedName("items")
    val repositoryResponseList: List<RepositoryResponse>
)