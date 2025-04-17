package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ChatMemberDto
import com.example.untitled_capstone.data.remote.dto.ChatRoomResponse
import com.example.untitled_capstone.data.remote.dto.ChatRoomRawDto
import com.example.untitled_capstone.data.remote.dto.MessageDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @POST("/api/chat/room/{roomId}/read")
    suspend fun readChats(
        @Path ("roomId") roomId: Long
    ): ApiResponse<Int>

    @POST("/api/chat/room/{roomId}/join")
    suspend fun joinChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<ChatRoomResponse>

    @POST("/api/chat/room/{roomId}/close")
    suspend fun closeChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<String>

    @GET("/api/chat/room/{roomId}")
    suspend fun enterChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<ChatRoomResponse>

    @GET("/api/chat/room/{roomId}/participants")
    suspend fun checkWhoIsIn(
        @Path("roomId") roomId: Long
    ): ApiResponse<List<ChatMemberDto>>

    @GET("/api/chat/room/{roomId}/messages/scroll")
    suspend fun getMessages(
        @Path("roomId") roomId: Long,
        @Query("lastMessageId") lastMessageId: Long? = null,
        @Query("size") size: Int = 20
    ): ApiResponse<List<MessageDto>>

    @GET("/api/chat/my-rooms")
    suspend fun getMyRooms(): ApiResponse<List<ChatRoomRawDto>>

    @DELETE("/api/chat/room/{roomId}/exit")
    suspend fun exitChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<String>
}