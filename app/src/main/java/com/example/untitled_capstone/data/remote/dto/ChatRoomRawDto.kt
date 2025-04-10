package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import java.time.LocalDateTime

data class ChatRoomRawDto(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int,
    val createdAt: String,
    val host: Boolean,
){
    fun toChattingRoomRaw(): ChattingRoomRaw {
        return ChattingRoomRaw(
            active = active,
            currentParticipants = currentParticipants,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime?.let { LocalDateTime.parse(it) },
            name = name,
            roomId = roomId,
            unreadCount = unreadCount,
            createdAt = createdAt.let { LocalDateTime.parse(it) },
            host = host
        )
    }
}