package com.wallace.githubtoprepositories.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntry(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "stars")val numberOfStars: Int,
    @ColumnInfo(name = "forks")val numberOfForks: Int,
    @ColumnInfo(name = "owner_name") val ownerName: String,
    @ColumnInfo(name = "owner_url") val ownerPictureProfile: String
)
