package com.example.untitled_capstone.feature.shopping.presentation.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun NewPostForm(navController: NavHostController) {
    var isExpandedPeopleMenu by remember { mutableStateOf(false) }
    var isExpandedCategoryMenu by remember { mutableStateOf(false) }
    val menuItemDataInPeople = List(10) { "${it + 1}" }
    val menuItemDataInCategory = listOf("식료품", "생활용품", "의류", "기타")
    var price by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var validator by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        image = uri
    }
    validator = price.isNotBlank() && title.isNotBlank() && content.isNotBlank()
    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    .background(CustomTheme.colors.onSurface)
                    .border(
                        width = 1.dp,
                        color = CustomTheme.colors.border,
                        shape = RoundedCornerShape(Dimens.cornerRadius)
                    )
            ){
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Center),
                    onClick = {
                        launcher.launch("image/*")
                    }
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.camera),
                        contentDescription = "get image",
                        tint = CustomTheme.colors.iconDefault,
                    )
                }
            }
            if(image != null){
                Box{
                    AsyncImage(
                        model = image,
                        contentDescription = "image",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .then(Modifier.size(24.dp))
                            .padding(Dimens.smallPadding),
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            contentDescription = "delete image",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.people),
                    contentDescription = "people",
                    tint = CustomTheme.colors.iconDefault,
                )
                Text(
                    text = "인원 수",
                    style = CustomTheme.typography.caption2,
                    color = CustomTheme.colors.textSecondary,
                )
            }
            Box{
                Card(
                    modifier = Modifier.wrapContentSize().clickable {
                        isExpandedPeopleMenu = !isExpandedPeopleMenu
                    },
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface,
                    ),
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.border),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "2",
                            style = CustomTheme.typography.caption2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                            contentDescription = "select number of people",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpandedPeopleMenu,
                    onDismissRequest = { isExpandedPeopleMenu = false },
                    containerColor = CustomTheme.colors.textTertiary,
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                ) {
                    menuItemDataInPeople.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = { /* Do something... */ },
                        )
                    }
                }
            }
            Text(
                text = "카테고리",
                style = CustomTheme.typography.caption2,
                color = CustomTheme.colors.textSecondary,
            )
            Box{
                Card(
                    modifier = Modifier.wrapContentSize().clickable {
                        isExpandedCategoryMenu = !isExpandedCategoryMenu
                    },
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomTheme.colors.onSurface,
                    ),
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.border),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "식료품",
                            style = CustomTheme.typography.caption2,
                            color = CustomTheme.colors.textPrimary,
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.dropdown),
                            contentDescription = "select number of people",
                            tint = CustomTheme.colors.iconSelected,
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpandedCategoryMenu,
                    onDismissRequest = { isExpandedCategoryMenu = false },
                    containerColor = CustomTheme.colors.textTertiary,
                    shape = RoundedCornerShape(Dimens.cornerRadius),
                ) {
                    menuItemDataInCategory.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTheme.typography.caption2,
                                    color = CustomTheme.colors.textPrimary,
                                )},
                            onClick = { /* Do something... */ },
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            // add validator
            value = price,
            onValueChange = {price = it},
            placeholder = {
                Text(
                    text = "가격",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textSecondary
                )
            },
            leadingIcon = {
                Text(
                    text = "₩",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textPrimary
                )
            },
            trailingIcon = {
                if(price.isNotBlank()){
                    IconButton(
                        onClick = { price = "" }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = "delete",
                        )
                    }
                }
            },
            textStyle = CustomTheme.typography.body1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CustomTheme.colors.textSecondary,
                unfocusedBorderColor = CustomTheme.colors.textSecondary,
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                errorCursorColor = CustomTheme.colors.error,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = title,
            onValueChange = {title = it},
            placeholder = {
                Text(
                    text = "제목",
                    style = CustomTheme.typography.body1,
                    color = CustomTheme.colors.textSecondary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CustomTheme.typography.body1,
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textPrimary,
                unfocusedTextColor = CustomTheme.colors.textPrimary,
                focusedContainerColor = CustomTheme.colors.onSurface,
                unfocusedContainerColor = CustomTheme.colors.onSurface,
                cursorColor = CustomTheme.colors.textPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                unfocusedTrailingIconColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.border
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = content,
                onValueChange = {content = it},
                placeholder = {
                    Text(
                        text = "내용을 입력하세요.",
                        style = CustomTheme.typography.body3,
                        color = CustomTheme.colors.textSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                textStyle = CustomTheme.typography.body3,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = CustomTheme.colors.textPrimary,
                    unfocusedTextColor = CustomTheme.colors.textPrimary,
                    focusedContainerColor = CustomTheme.colors.onSurface,
                    unfocusedContainerColor = CustomTheme.colors.onSurface,
                    cursorColor = CustomTheme.colors.textPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                    unfocusedTrailingIconColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.primary,
                disabledContainerColor = CustomTheme.colors.onSurface,
                contentColor = CustomTheme.colors.onPrimary,
                disabledContentColor = CustomTheme.colors.textTertiary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CustomTheme.colors.border
            ),
            enabled = validator,
            onClick = { navController.popBackStack() }
        ) {
            Text(
                text = "등록하기",
                style = CustomTheme.typography.button1,
            )
        }
    }
}