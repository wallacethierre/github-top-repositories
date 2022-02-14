package com.wallace.githubtoprepositories.model

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val numberOfStars: Int,
    @SerializedName("forks_count")
    val numberOfForks: Int,
    val owner: Owner
)
