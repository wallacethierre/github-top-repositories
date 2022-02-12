package com.wallace.githubtoprepositories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallace.githubtoprepositories.adapter.RepositoryAdapter
import com.wallace.githubtoprepositories.databinding.ActivityMainBinding
import com.wallace.githubtoprepositories.viewmodel.GithubRepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repoAdapter: RepositoryAdapter
    private val githubRepoViewMode: GithubRepoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycleView()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            githubRepoViewMode.listOfRepository.collect { pagingData ->
                repoAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecycleView() {
        repoAdapter = RepositoryAdapter()
        binding.recyclerView.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

//        githubRepoViewMode.responseRepositories.observe(this) { repoList ->
//            if (repoList != null) {
//                repoAdapter.submitList(repoList)
//            }
//        }
    }

}