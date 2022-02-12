package com.wallace.githubtoprepositories.remote

import com.google.gson.annotations.SerializedName
import com.wallace.githubtoprepositories.model.Repository

data class RepositoryResponse (
    @SerializedName("items")
    val repositoryList: List<Repository>
)