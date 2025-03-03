package com.example.untitled_capstone.presentation.feature.shopping.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.shopping.composable.PostListContainer
import com.example.untitled_capstone.presentation.feature.shopping.state.PostState

@Composable
fun ShoppingScreen(navController: NavHostController, state: PostState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.surfaceHorizontalPadding,
            vertical = Dimens.surfaceVerticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ) {
        items(state.posts) { post ->
            Box(
                modifier = Modifier.clickable {
                    navController.navigate(
                        Screen.PostNav(
                            post = post
                        )
                    )
                }
            ){
                PostListContainer(post)
            }
        }
    }
}