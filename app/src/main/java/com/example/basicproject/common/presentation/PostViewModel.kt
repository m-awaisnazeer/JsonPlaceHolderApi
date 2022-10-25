package com.example.basicproject.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.common.domain.PostRepository
import com.example.basicproject.common.utils.DispatcherProvider
import com.example.basicproject.common.utils.Resource
import com.example.basicproject.common.utils.createExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val dispatcherProvider: DispatcherProvider,
                    private val repository: PostRepository) : ViewModel() {

    private val _postUiState:MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.Loading)
    val postUiState:StateFlow<PostUiState> = _postUiState

    init {
        getPosts()
    }

    private fun getPosts() {
        val unknownError = "An UnKnown Error occoured"
        val exceptionHandler = viewModelScope.createExceptionHandler {
            _postUiState.value = PostUiState.Error(it.message?:unknownError)
        }

        val context = dispatcherProvider.IO+exceptionHandler
        viewModelScope.launch(context) {
            repository.getPosts().collect {
                when (it) {
                    is Resource.Success -> {
                        _postUiState.value = PostUiState.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _postUiState.value = PostUiState.Error(it.message!!)
                    }
                }
            }
        }
    }
}