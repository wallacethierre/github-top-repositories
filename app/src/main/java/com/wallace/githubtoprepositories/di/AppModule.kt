package com.wallace.githubtoprepositories.di

import android.app.Application
import com.wallace.githubtoprepositories.Constants
import com.wallace.githubtoprepositories.db.PageIndexDao
import com.wallace.githubtoprepositories.db.RepositoryDao
import com.wallace.githubtoprepositories.db.RepositoryDatabase
import com.wallace.githubtoprepositories.remote.GithubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getAppDatabase(context: Application): RepositoryDatabase {
        return RepositoryDatabase.getAppInstance(context)
    }

    @Provides
    @Singleton
    fun getRepositoryDao(appDatabase: RepositoryDatabase): RepositoryDao {
        return appDatabase.getRepositoryDao()
    }

    @Provides
    @Singleton
    fun getPageIndexDao(appDatabase: RepositoryDatabase): PageIndexDao {
        return appDatabase.getPageIndexDao()
    }

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL


    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }).build()

    @Provides
    @Singleton
    fun getRetrofitInstance(BASE_URL: String): GithubApiService =
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(GithubApiService::class.java)

}