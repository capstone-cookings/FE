package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.PostItemEntity

data class PostRawDto(
    val id: Long,
    val timeAgo: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String,
    val district: String,
    val neighborhood: String
){
    fun toPostEntity(): PostItemEntity{
        return  PostItemEntity(
            id = id,
            timeAgo = timeAgo,
            district = district,
            neighborhood = neighborhood,
            likeCount = likeCount,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}