package com.wallace.githubtoprepositories.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallace.githubtoprepositories.model.Repository
import com.wallace.githubtoprepositories.repository.GithubRepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val githubRepRepository: GithubRepRepository) :
    ViewModel() {

    private val _response = MutableLiveData<List<Repository>>()

    val responseRepositories: LiveData<List<Repository>> get() = _response

    init {
        getAllRepository()
    }

    private fun getAllRepository() = viewModelScope.launch {
        githubRepRepository.getAllGithubRepositories("stars", 1, 50).let { response ->
            if (response.isSuccessful) {
                _response.postValue(response.body()!!.repositoryList)
            } else {
                Log.d("TAG", "Error to load repositories list: ${response.errorBody()}")
            }
        }
    }
}