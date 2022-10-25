package com.example.basicproject.common.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicproject.common.data.Api
import com.example.basicproject.common.data.PostRepositoryImp
import com.example.basicproject.common.domain.Post
import com.example.basicproject.common.utils.DefaultDispatcher
import com.example.basicproject.databinding.ActivityMainBinding
import com.example.basicproject.posts_list.PostsAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = PostRepositoryImp(getPostApi())
                return PostViewModel(DefaultDispatcher(), repository) as T
            }
        }
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
        setUpUi()
        subscribeToPosts()
    }

    private fun setUpUi() {
        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }


    private fun subscribeToPosts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postUiState.collect {
                    when(it){
                        is PostUiState.Success -> {
                            binding.postsRecyclerView.visibility = View.VISIBLE
                            binding.postsProgressBar.visibility  = View.GONE
                            binding.postsRecyclerView.adapter = PostsAdapter(it.data)
                        }
                        is PostUiState.Error -> {
                            binding.postsProgressBar.visibility  = View.GONE
                            Toast.makeText(this@MainActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                        PostUiState.Loading -> {
                            binding.postsProgressBar.visibility  = View.VISIBLE
                            binding.postsRecyclerView.visibility  = View.GONE
                        }
                    }
                }
            }
        }
    }

    fun getPostApi(baseUrl: String = "https://jsonplaceholder.typicode.com"): Api {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(Api::class.java)
    }
}

sealed class PostUiState{
    data class Success(val data:List<Post>):PostUiState()
    data class Error(val message:String):PostUiState()
    object Loading:PostUiState()
}