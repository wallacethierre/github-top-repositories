package com.wallace.githubtoprepositories.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wallace.githubtoprepositories.model.Repository
import com.wallace.githubtoprepositories.paging.RepositoryPagingSource
import com.wallace.githubtoprepositories.repository.GithubRepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val githubRepRepository: GithubRepRepository) :
    ViewModel() {

//    private val _response = MutableLiveData<List<Repository>>()
//
//    val responseRepositories: LiveData<List<Repository>> get() = _response
//
//    init {
//        getAllRepository()
//    }

    val listOfRepository = Pager(PagingConfig(pageSize = 30)) {
        RepositoryPagingSource(githubRepRepository, "stars")
    }.flow.cachedIn(viewModelScope)

//    private fun getAllRepository() = viewModelScope.launch {
//        githubRepRepository.getAllGithubRepositories("stars", 30).let { response ->
//            if (response.isSuccessful) {
//                _response.postValue(response.body()!!.repositoryList)
//            } else {
//                Log.d("TAG", "Error to load repositories list: ${response.errorBody()}")
//            }
//        }
//    }
}