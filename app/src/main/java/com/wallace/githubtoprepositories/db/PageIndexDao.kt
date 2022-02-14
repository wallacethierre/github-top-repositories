package com.wallace.githubtoprepositories.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PageIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeyEntry: PageIndexEntry)

    @Query("SELECT * FROM page_index WHERE id = :query")
    suspend fun searchRemoteKeyById(query: String): PageIndexEntry?

    @Query("DELETE FROM page_index where :key")
    suspend fun deleteByKey(key: String)
}