package com.wallace.githubtoprepositories.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories ORDER BY stars DESC")
    fun getAll(): PagingSource<Int, RepositoryEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositoryEntry: List<RepositoryEntry>)

    @Query("DELETE FROM repositories")
    suspend fun deleteAll()
}