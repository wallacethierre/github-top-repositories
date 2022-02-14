package com.wallace.githubtoprepositories.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_index")
data class PageIndexEntry(
    @PrimaryKey
    val id: String,
    val nextPage: Int?
)
