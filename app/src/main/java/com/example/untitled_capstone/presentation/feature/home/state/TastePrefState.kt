package com.example.untitled_capstone.presentation.feature.home.state

data class TastePrefState(
    val tastePref: String? = null,
    val loading: Boolean = false,
    val error: String? = null
)