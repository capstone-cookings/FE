package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.data.local.db.MessageItemDatabase
import com.example.untitled_capstone.data.remote.dto.MessageDto
import com.example.untitled_capstone.data.remote.manager.WebSocketManager
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.model.UnreadBroadcast
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val db: MessageItemDatabase
) : WebSocketRepository {

    override fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        webSocketManager.connect(token, roomId, onConnected, onError)
    }

    override suspend fun subscribeRoom(
        roomId: Long,
        onMessage: (MessageDto) -> Unit,
        onUnreadUpdate: (UnreadBroadcast) -> Unit
    ) {
        webSocketManager.subscribeRoom(
            roomId = roomId,
            onMessage = { dto ->
                CoroutineScope(Dispatchers.IO).launch {
                    saveMessageToDatabase(dto, roomId)
                }
                onMessage(dto) },
            onUnreadUpdate = { dto ->
                CoroutineScope(Dispatchers.IO).launch {
                    updateUnreadCount(dto.messageId, roomId, dto.unreadCount )
                }
                onUnreadUpdate(dto.toUnreadBroadcast())
            }
        )
    }

    override fun sendMessage(roomId: Long, content: String) {
        webSocketManager.sendMessage(roomId, content)
    }

    override fun sendReadEvent(roomId: Long) {
        webSocketManager.sendReadEvent(roomId)
    }

    override fun disconnect() {
        webSocketManager.disconnect()
    }

    private suspend fun saveMessageToDatabase(message: MessageDto, roomId: Long) {
        db.dao.insert(message.toMessageEntity(roomId))
    }

    private suspend fun updateUnreadCount(messageId: Long, roomId: Long, unreadCount: Int) {
        db.dao.updateUnreadCount(messageId, roomId, unreadCount)
    }
}