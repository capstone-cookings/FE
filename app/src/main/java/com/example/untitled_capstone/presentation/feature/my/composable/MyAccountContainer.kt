package com.example.untitled_capstone.presentation.feature.my.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Graph
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.login.LoginEvent
import com.example.untitled_capstone.presentation.feature.my.MyEvent
import com.example.untitled_capstone.presentation.feature.my.MyState
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MyAccountContainer(navigateTo: () -> Unit, state: MyState) {
    Card(
        modifier = Modifier.clickable { navigateTo()},
        shape = RoundedCornerShape(Dimens.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colors.onSurface
        )
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.profile),
                    contentDescription = "profile",
                )
                if(state.isLoggedIn){
                    Text(
                        text = state.profile?.nickname ?: "USER",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                }else{
                    Text(
                        text = "로그인하기",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                }
            }
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                contentDescription = "navigate",
                tint = CustomTheme.colors.iconDefault
            )
        }
    }
}