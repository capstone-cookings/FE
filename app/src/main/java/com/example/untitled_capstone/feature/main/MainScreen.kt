package com.example.untitled_capstone.feature.main

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
import com.example.untitled_capstone.navigation.Navigation
import com.example.untitled_capstone.R
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()
    val screens = listOf(
        BottomScreen.Home,
        BottomScreen.Shopping,
        BottomScreen.Refrigerator,
        BottomScreen.Chat,
        BottomScreen.My
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    Scaffold(
        containerColor = CustomTheme.colors.surface,
        topBar = {
            when(currentDestination?.route){
                BottomScreen.Home.route -> TopBar()
                BottomScreen.Shopping.route -> TopBar()
                BottomScreen.Refrigerator.route -> TopBar()
                BottomScreen.Chat.route -> TopBar()
                BottomScreen.My.route -> TopBar()
            }
        },
        bottomBar = {
            if(bottomBarDestination){
                BottomNavBar(navController = navController, viewModel = viewModel)
            }
        },
        floatingActionButton = {
            if(viewModel.selectedIndex == 0 && bottomBarDestination){
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
        }
    ){  innerPadding ->
        if(bottomBarDestination) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ){
                Navigation(navController = navController)
            }
        }else{
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                Navigation(navController = navController)
            }
        }
    }
}