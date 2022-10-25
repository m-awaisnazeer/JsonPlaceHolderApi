package com.example.basicproject.common.data

import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/posts")
    suspend fun getPostList(): Response<List<PostDTO?>?>
}