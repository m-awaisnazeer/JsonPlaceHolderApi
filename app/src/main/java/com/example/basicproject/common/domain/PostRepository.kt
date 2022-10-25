package com.example.basicproject.common.domain

import com.example.basicproject.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(): Flow<Resource<List<Post>>>
}