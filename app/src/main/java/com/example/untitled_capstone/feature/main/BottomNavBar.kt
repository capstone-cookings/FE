package com.example.untitled_capstone.feature.main

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun BottomNavBar(navController: NavHostController, viewModel: MainViewModel){
    val items = listOf(
        BottomNavItem(
            title = "홈",
            route = "home",
            icon = ImageVector.vectorResource(id = R.drawable.home)
        ),
        BottomNavItem(
            title = "공동구매",
            route = "shopping",
            icon = ImageVector.vectorResource(id = R.drawable.shopping)
        ),
        BottomNavItem(
            title = "냉장고",
            route = "refrigerator",
            icon = ImageVector.vectorResource(id = R.drawable.refrigerator)
        ),
        BottomNavItem(
            title = "채팅",
            route = "chatting",
            icon = ImageVector.vectorResource(id = R.drawable.chat)
        ),
        BottomNavItem(
            title = "My",
            route = "my",
            icon = ImageVector.vectorResource(id = R.drawable.my)
        )
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = CustomTheme.colors.onSurface
    ) {
        items.forEachIndexed() { index, item ->
            NavigationBarItem(
                modifier = Modifier.padding(3.dp),
                selected = viewModel.selectedIndex == index,
                onClick = {
                    viewModel.updateSelectedIndex(index)
                    navController.navigate(item.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontWeight = CustomTheme.typography.button2.fontWeight,
                        fontSize = CustomTheme.typography.button2.fontSize,
                        fontFamily = CustomTheme.typography.button2.fontFamily,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CustomTheme.colors.iconSelected,
                    unselectedIconColor = CustomTheme.colors.iconDefault,
                    selectedTextColor = CustomTheme.colors.textPrimary,
                    unselectedTextColor = CustomTheme.colors.textSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}