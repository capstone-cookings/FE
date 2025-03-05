package com.example.untitled_capstone.presentation.feature.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.navigation.NavigationV2
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.composable.MyTopBar
import com.example.untitled_capstone.presentation.feature.refrigerator.composable.FridgeTopBar
import com.example.untitled_capstone.presentation.feature.shopping.composable.ShoppingTopBar
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()
    val screens = listOf(
        Screen.Home.toString(),
        Screen.Shopping.toString(),
        Screen.Fridge.toString(),
        Screen.Chat.toString(),
        Screen.My.toString()
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = navBackStackEntry?.destination?.route
    val bottomRoute = route?.split(".")?.lastOrNull()
    val bottomBarDestination = screens.any { bottomRoute.equals(it) }

    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            when{
                bottomRoute.equals(screens[0]) -> TopBar(1, navController)
                bottomRoute.equals(screens[1]) -> ShoppingTopBar(navController)
                bottomRoute.equals(screens[2]) -> FridgeTopBar(navController, viewModel)
                bottomRoute.equals(screens[3]) -> TopBar(4, navController)
                bottomRoute.equals(screens[4]) -> MyTopBar(navController)
            }
        },
        bottomBar = {
            if(bottomBarDestination){
                BottomNavBar(currentDestination = currentDestination, navController = navController)
            }
        },
        floatingActionButton = {
            if(bottomBarDestination){
                when (bottomRoute) {
                    screens[0] -> {
                        FloatingActionButton(
                            onClick = { /*TODO*/ },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = Color.Unspecified,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ai_big),
                                contentDescription = "ai"
                            )
                        }
                    }
                    screens[1], screens[2] -> {
                        FloatingActionButton(
                            onClick = {
                                if (bottomRoute == screens[1]) navController.navigate(Screen.WritingNav)
                                else navController.navigate(Screen.AddFridgeItemNav)
                            },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = Color.Unspecified,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.writing),
                                contentDescription = "write new post"
                            )
                        }
                    }
                }
            }
        }
    ){  innerPadding ->
        if(bottomBarDestination) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ){
                NavigationV2(navController = navController, viewModel)
            }
        }else{
            NavigationV2(navController = navController, viewModel)
        }
    }
}