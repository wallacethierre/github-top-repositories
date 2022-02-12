package com.wallace.githubtoprepositories.di

import com.wallace.githubtoprepositories.Constants
import com.wallace.githubtoprepositories.remote.GithubApiService
import com.wallace.githubtoprepositories.repository.GithubRepRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun getRetrofitInstance(BASE_URL: String): GithubApiService = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(GithubApiService::class.java)

}