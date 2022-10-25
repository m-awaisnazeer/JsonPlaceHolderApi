package com.example.basicproject.posts_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basicproject.common.domain.Post
import com.example.basicproject.databinding.PostItemBinding

class PostsAdapter(val posts: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: PostItemBinding

        constructor(binding: PostItemBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(post: Post) {
            binding.txtTitle.text = post.title
            binding.txtDescription.text = post.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val noteItemBinding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(noteItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NoteViewHolder).bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}