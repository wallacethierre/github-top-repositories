package com.wallace.githubtoprepositories.data.remote

import com.google.gson.annotations.SerializedName
import com.wallace.githubtoprepositories.model.RepositoryResponse

data class GithubResponse (
    @SerializedName("items")
    val repositoryResponseList: List<RepositoryResponse>
)