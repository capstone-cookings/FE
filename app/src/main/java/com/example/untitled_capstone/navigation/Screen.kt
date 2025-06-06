package com.example.untitled_capstone.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data class ChattingRoomNav(val id: Long) : Screen

    @Serializable
    data class RecipeNav(val id: Long) : Screen

    @Serializable
    data class PostDetailNav(val id: Long) : Screen

    @Serializable
    data object WritingNav: Screen

    @Serializable
    data object NotificationNav: Screen

    @Serializable
    data object OnBoarding: Screen

    @Serializable
    data class AddFridgeItemNav(val id: Long?): Screen

    @Serializable
    data object  LoginNav: Screen

    @Serializable
    data object Home: Screen

    @Serializable
    data object Post: Screen

    @Serializable
    data object Fridge: Screen

    @Serializable
    data object Chat: Screen

    @Serializable
    data object My: Screen

    @Serializable
    data class Profile(val nickname: String?): Screen

    @Serializable
    data object NicknameNav: Screen

    @Serializable
    data object LocationNav: Screen

    @Serializable
    data object MyPostNav: Screen

    @Serializable
    data object MyLikedPostNav: Screen

    @Serializable
    data object ScanNav: Screen

    @Serializable
    data object PostSearchNav: Screen

    @Serializable
    data object RecipeModifyNav: Screen

    @Serializable
    data class ChattingDrawerNav(val id: Long, val title: String): Screen

    @Serializable
    data class ReportPostNav(val postId: Long): Screen
}

sealed interface Graph {
    @Serializable
    data object HomeGraph: Graph

    @Serializable
    data object PostGraph: Graph

    @Serializable
    data object FridgeGraph: Graph

    @Serializable
    data object ChatGraph: Graph

    @Serializable
    data object MyGraph: Graph

    @Serializable
    data object LoginGraph: Graph

    @Serializable
    data object  OnBoardingGraph: Graph
}
