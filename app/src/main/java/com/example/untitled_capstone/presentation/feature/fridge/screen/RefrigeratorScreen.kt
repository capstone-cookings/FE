package com.example.untitled_capstone.presentation.feature.fridge.screen

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.fridge.composable.FridgeItemContainer
import com.example.untitled_capstone.presentation.feature.fridge.composable.PermissionDialog
import com.example.untitled_capstone.presentation.feature.fridge.FridgeAction
import com.example.untitled_capstone.presentation.feature.fridge.FridgeState
import com.example.untitled_capstone.ui.theme.CustomTheme


@Composable
fun RefrigeratorScreen(fridgeItems: LazyPagingItems<FridgeItem>, state: FridgeState, viewModel: MainViewModel, onAction: (FridgeAction) -> Unit, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = listOf("등록 순", "유통기한 순")
    var alignMenu by remember { mutableStateOf("등록 순") }
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.surfaceHorizontalPadding,
                vertical = Dimens.surfaceVerticalPadding
            ),
        horizontalAlignment = Alignment.End
    ) {
        Column {
            Row(
                modifier = Modifier.clickable {
                    expanded = !expanded
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = alignMenu,
                    style = CustomTheme.typography.button2,
                    color = CustomTheme.colors.textPrimary,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                    contentDescription = "open dropdown menu",
                    tint = CustomTheme.colors.iconSelected,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = CustomTheme.colors.textTertiary,
                shape = RoundedCornerShape(Dimens.cornerRadius),
            ) {
                menuItemData.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(30.dp),
                        text = {
                            Text(
                                text = option,
                                style = CustomTheme.typography.caption1,
                                color = CustomTheme.colors.textPrimary,
                            )
                        },
                        onClick = {
                            expanded = false
                            alignMenu = option
                            if(option == menuItemData[0]){
                                onAction(FridgeAction.GetItems)
                            } else {
                                onAction(FridgeAction.GetItemsByDate)
                            }
                        },
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.border
        )
        Spacer(modifier = Modifier.height(Dimens.mediumPadding))
        LaunchedEffect(key1 = fridgeItems.loadState) {
            if(fridgeItems.loadState.refresh is LoadState.Error) {
                Log.d("error", (fridgeItems.loadState.refresh as LoadState.Error).error.message.toString())
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if(fridgeItems.loadState.refresh is LoadState.Loading || state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
                ) {
                    items(count = fridgeItems.itemCount) { index ->
                        val item = fridgeItems[index]
                        if (item != null && item.isFridge == viewModel.topSelector) {
                            FridgeItemContainer(
                                item,
                                onAction,
                                onShowDialog = { showDialog.value = true },
                                navigateToModifyItemScreen = {
                                    navController.navigate(
                                        Screen.AddFridgeItemNav(id = item.id)) },
                            )
                        }
                    }
                    item {
                        if (fridgeItems.loadState.append is LoadState.Loading && fridgeItems.itemCount > 10) {
                            CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }
    }
    PermissionDialog(
        showDialog = showDialog,
        message = "알람 권한이 필요합니다.",
        onDismiss = { showDialog.value = false },
        onConfirm = {
            showDialog.value = false
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)
        }
    )
}