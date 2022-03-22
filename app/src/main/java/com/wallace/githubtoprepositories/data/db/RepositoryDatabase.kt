package com.wallace.githubtoprepositories.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RepositoryEntry::class, PageIndexEntry::class], version = 1, exportSchema = false)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract fun getRepositoryDao(): RepositoryDao
    abstract fun getPageIndexDao(): PageIndexDao
    companion object {
        private const val DB_NAME = "github_repo.db"
        @Volatile
        private var DB_INSTANCE: RepositoryDatabase? = null

        fun getAppInstance(context: Context): RepositoryDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RepositoryDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return DB_INSTANCE!!
        }
    }
}