package com.example.untitled_capstone.domain.model

data class PostRaw(
    val id: Long,
    val category: String,
    val content: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String
)
