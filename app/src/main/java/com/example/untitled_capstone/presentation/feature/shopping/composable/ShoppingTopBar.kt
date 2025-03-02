package com.example.untitled_capstone.presentation.feature.shopping.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingTopBar(navController: NavHostController){
    TopAppBar(
        modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
        title = {
            Row (
                modifier = Modifier.clickable {
                        TODO("change location")
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "무거동", //todo(state로 변경),
                    fontFamily = CustomTheme.typography.headline3.fontFamily,
                    fontWeight = CustomTheme.typography.headline3.fontWeight,
                    fontSize = CustomTheme.typography.headline3.fontSize,
                    color = CustomTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_down),
                    contentDescription = "dropdown",
                    tint = CustomTheme.colors.iconSelected
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search),
                    tint = CustomTheme.colors.iconDefault,
                    contentDescription = "search"
                )
            }
            IconButton(
                onClick = {navController.navigate(Screen.NotificationNav)}
            ) {
                Icon(
                    tint = CustomTheme.colors.iconDefault,
                    imageVector = ImageVector.vectorResource(R.drawable.bell),
                    contentDescription = "alarm"
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = CustomTheme.colors.surface,
            scrolledContainerColor = CustomTheme.colors.surface,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        )
    )
}