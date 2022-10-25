package com.example.basicproject.common.data

import com.example.basicproject.common.domain.Post

data class PostDTO(
    val userId: Int?,
    val id: Int?,
    val title: String?,
    val body: String?
){

   companion object{
       fun toPost(postDTO: PostDTO):Post{
           return Post(postDTO.title?:"",postDTO.body?:"")
       }
   }
}