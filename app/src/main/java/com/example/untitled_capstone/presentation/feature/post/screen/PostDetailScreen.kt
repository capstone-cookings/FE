package com.example.untitled_capstone.presentation.feature.post.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.post.composable.PostContainer
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.util.UiState
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    id: Long,
    nickname: String,
    state: UiState,
    post: Post?,
    onEvent: (PostEvent) -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    var menuItem by remember { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(true) {
        onEvent(PostEvent.GetPostById(id))
    }
    LaunchedEffect(post) {
        if(post != null){
            menuItem = if(nickname == post.nickname) listOf("수정", "삭제") else listOf("신고")
        }
    }
    if(state is UiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    }
    if(post != null){
        Scaffold(
            containerColor = CustomTheme.colors.onSurface,
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onEvent(PostEvent.PopBackStack)
                                onEvent(PostEvent.InitState)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "back",
                            )
                        }
                    },
                    title = {},
                    actions = {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.share),
                                tint = CustomTheme.colors.iconDefault,
                                contentDescription = "share"
                            )
                        }
                        IconButton(
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                tint = CustomTheme.colors.iconDefault,
                                imageVector = ImageVector.vectorResource(R.drawable.more),
                                contentDescription = "menu"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = CustomTheme.colors.textTertiary,
                            shape = RoundedCornerShape(Dimens.cornerRadius),
                        ) {
                            menuItem.forEach { option ->
                                DropdownMenuItem(
                                    modifier = Modifier.height(30.dp).width(90.dp),
                                    text = {
                                        Text(
                                            text = option,
                                            style = CustomTheme.typography.caption1,
                                            color = CustomTheme.colors.textPrimary,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        if(menuItem.size == 1){
                                            onEvent(PostEvent.NavigateUp(
                                                Screen.ReportPostNav(
                                                    postId = post.id
                                                )
                                            ))
                                        }else if(menuItem.size == 2){
                                            when(option){
                                                menuItem[0] -> onEvent(PostEvent.NavigateUp(Screen.WritingNav))
                                                menuItem[1] -> {
                                                    onEvent(PostEvent.DeletePost(post.id))
                                                    onEvent(PostEvent.PopBackStack)
                                                    onEvent(PostEvent.InitState)
                                                }
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CustomTheme.colors.onSurface
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.wrapContentHeight(),
                    containerColor = CustomTheme.colors.onSurface,
                    content = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = Dimens.largePadding),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                modifier = Modifier.height(80.dp)
                                    .padding(vertical = Dimens.largePadding),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        onEvent(PostEvent.ToggleLike(post.id))
                                    }
                                ) {
                                    if(post.liked){
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.heart_filled),
                                            contentDescription = "like",
                                            tint = CustomTheme.colors.iconRed,
                                        )
                                    }else{
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.heart),
                                            contentDescription = "like",
                                            tint = CustomTheme.colors.iconDefault
                                        )
                                    }
                                }
                                VerticalDivider(
                                    modifier = Modifier.padding(horizontal = 6.dp).height(80.dp),
                                    thickness = 1.dp,
                                    color = CustomTheme.colors.textTertiary
                                )
                                Text(
                                    text = post.price.toString() + " 원",
                                    style = CustomTheme.typography.title2,
                                    color = CustomTheme.colors.textPrimary,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.height(80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier.padding(end = 4.dp),
                                    onClick = {
                                        onEvent(PostEvent.NavigateUp(
                                            Screen.ChattingRoomNav(post.chatRoomId)
                                        ))
                                    },
                                    enabled = post.roomActive && post.currentParticipants < post.memberCount,
                                    shape = RoundedCornerShape(Dimens.cornerRadius),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CustomTheme.colors.primary,
                                        contentColor = CustomTheme.colors.textPrimary,
                                        disabledContainerColor = CustomTheme.colors.borderLight,
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "채팅하기",
                                        style = CustomTheme.typography.button1,
                                        color = CustomTheme.colors.onPrimary
                                    )
                                }
                                Icon(
                                    imageVector  = ImageVector.vectorResource(R.drawable.people),
                                    contentDescription = "numberOfPeople",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                                Text(
                                    text = "${post.currentParticipants}/${post.memberCount}",
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textSecondary,
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
                    .padding(horizontal = Dimens.surfaceHorizontalPadding,
                        vertical = Dimens.surfaceVerticalPadding),
            ){
                PostContainer(
                    post= post,
                    goToProfile = {
                        onEvent(
                            PostEvent.NavigateUp(
                                Screen.Profile(
                                    post.nickname
                                )
                            )
                        )
                    }
                )
            }
        }
    }
}