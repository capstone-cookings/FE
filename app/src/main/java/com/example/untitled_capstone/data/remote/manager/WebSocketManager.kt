package com.example.untitled_capstone.data.remote.manager

import android.annotation.SuppressLint
import com.example.untitled_capstone.core.util.Constants.WS_URL
import com.example.untitled_capstone.data.remote.dto.MessageDto
import com.example.untitled_capstone.data.remote.dto.UnreadBroadcastDto
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

class WebSocketManager @Inject constructor() {
    private var stompClient: StompClient? = null
    private var isReconnecting = false

    @SuppressLint("CheckResult")
    fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL)
        val headers = listOf(
            StompHeader("Authorization", "Bearer $token")
        )
        stompClient?.lifecycle()?.subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    isReconnecting = false
                    onConnected()
                }
                LifecycleEvent.Type.ERROR -> {
                    if (!isReconnecting) {
                        isReconnecting = true
                        retryConnect(token, roomId, onConnected, onError)
                    }
                    onError(event.exception)
                }
                LifecycleEvent.Type.CLOSED -> {
                    if (!isReconnecting) {
                        isReconnecting = true
                        retryConnect(token, roomId, onConnected, onError)
                    }
                }
                else -> {}
            }
        }
        stompClient?.connect(headers)
    }

    @SuppressLint("CheckResult")
    fun subscribeRoom(roomId: Long, onMessage: (MessageDto) -> Unit, onUnreadUpdate: (UnreadBroadcastDto) -> Unit) {
        stompClient?.topic("/sub/chat/room/$roomId")?.subscribe { stompMessage ->
            val message = Gson().fromJson(stompMessage.payload, MessageDto::class.java)
            onMessage(message)
        }

        stompClient?.topic("/sub/chat/room/$roomId/unread")?.subscribe { stompMessage ->
            val unread = Gson().fromJson(stompMessage.payload, UnreadBroadcastDto::class.java)
            onUnreadUpdate(unread)
        }
    }

    fun sendMessage(roomId: Long, content: String) {
        val payload = Gson().toJson(mapOf("roomId" to roomId, "content" to content))
        stompClient?.send("/pub/chat/message", payload)?.subscribe()
    }

    fun sendReadEvent(roomId: Long) {
        val payload = Gson().toJson(mapOf("roomId" to roomId))
        stompClient?.send("/pub/chat/read", payload)?.subscribe()
    }

    fun disconnect() {
        stompClient?.disconnect()
    }

    private fun retryConnect(
        token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000) // 5초 간격으로 재시도
            connect(token, roomId, onConnected, onError)
        }
    }
}