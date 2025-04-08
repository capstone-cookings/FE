package com.example.untitled_capstone.presentation.feature.chat.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.untitled_capstone.domain.model.ChattingRoom

class ChattingRoomState {
    var response by mutableStateOf<ChattingRoom?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}
