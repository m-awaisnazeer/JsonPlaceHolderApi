package com.example.basicproject.common.data

import com.example.basicproject.common.data.PostDTO.Companion.toPost
import com.example.basicproject.common.domain.Post
import com.example.basicproject.common.domain.PostRepository
import com.example.basicproject.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepositoryImp(private val api: Api):PostRepository {
    override suspend fun getPosts(): Flow<Resource<List<Post>>> = flow {
        val response = api.getPostList()
        if (response.isSuccessful){
            response.body()?.let {
                val posts:List<Post> = it.filterNotNull().map { toPost(it) }
                emit(Resource.Success(posts))
            }?: kotlin.run { emit(Resource.Success(emptyList())) }
        }else{
            emit(Resource.Error(null,response.message()))
        }
    }
}