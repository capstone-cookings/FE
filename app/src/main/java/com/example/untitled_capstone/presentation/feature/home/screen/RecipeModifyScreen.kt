package com.example.untitled_capstone.presentation.feature.home.screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.untitled_capstone.MainActivity
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.presentation.feature.fridge.composable.PermissionDialog
import com.example.untitled_capstone.presentation.feature.home.HomeEvent
import com.example.untitled_capstone.presentation.feature.home.state.ModifyState
import com.example.untitled_capstone.presentation.feature.my.composable.getRealPathFromURI
import com.example.untitled_capstone.ui.theme.CustomTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeModifyScreen(recipe: Recipe, state: ModifyState, onEvent: (HomeEvent) -> Unit, navigateToBack: () -> Unit) {
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf( recipe.title)}
    val ingredients = remember {
        recipe.ingredients.map { mutableStateOf(it) }.toMutableStateList()
    }
    var instructions by remember { mutableStateOf( recipe.instructions)}
    val listState = rememberLazyListState()
    var validator =
        if (title.isNotEmpty() &&
            ingredients.isNotEmpty() &&
            ingredients.toList().first().value.isNotBlank() &&
            instructions.isNotEmpty()) true else false

    val context = LocalContext.current
    var image by remember { mutableStateOf(recipe.imageUrl) }
    val showDialog = remember { mutableStateOf(false) }
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            image = uri.toString()
                            val filePath = context.getRealPathFromURI(it)
                            if (filePath != null) {
                                onEvent(HomeEvent.UploadImage(recipe.id ,File(filePath)))
                            }
                            Log.d("TargetSDK", "imageUri - selected : $uri")
                        }
                    }
                }
                Activity.RESULT_CANCELED -> Unit
            }
        }
    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val galleryPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                galleryPermissions.forEach { permission ->
                    if (permissions[permission] == true){
                        Log.d("gallery", "gallery permission granted")
                    }
                }
            }
        )

    LaunchedEffect(ingredients.size) {
        if (ingredients.isNotEmpty()) {
            listState.animateScrollToItem(ingredients.size + 3)
        }
    }
    LaunchedEffect(state.isSuccess) {
        if(state.isSuccess){
            navigateToBack()
        }
    }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateToBack()
                            onEvent(HomeEvent.InitState)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "back",
                        )
                    }
                },
                title = {
                    Text(
                        text = "레시피",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            thickness = 1.dp,
            color = CustomTheme.colors.borderLight
        )
        PermissionDialog(
            showDialog = showDialog,
            message = "이미지를 업로드하려면 저장소 접근 권한이 필요합니다.",
            onDismiss = { showDialog.value = false },
            onConfirm = {
                showDialog.value = false
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
        )
        if(state.loading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = CustomTheme.colors.primary
                )
            }
        }
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    horizontal = Dimens.largePadding,
                    vertical = Dimens.mediumPadding
                )
                .fillMaxSize()
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = {
                            Text(
                                text = "제목",
                                style = CustomTheme.typography.title1,
                                color = CustomTheme.colors.textPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    title = ""
                                }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.delete),
                                    contentDescription = "delete",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = CustomTheme.typography.title1,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = CustomTheme.colors.border,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.borderLight
                    )
                }
            }
            item{
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        .background(CustomTheme.colors.surface),
                    contentAlignment = Alignment.BottomEnd,
                ){
                    if (image != null) {
                        AsyncImage(
                            model = image,
                            contentDescription = recipe.title,
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                        )
                    }
                    Row {
                        IconButton(
                            onClick = {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        galleryPermissions[0]
                                    ) == PackageManager.PERMISSION_GRANTED ->  {
                                        albumLauncher.launch(imageAlbumIntent)
                                    }
                                    shouldShowRequestPermissionRationale(
                                        context as MainActivity,
                                        galleryPermissions[0]
                                    ) -> {
                                        showDialog.value = true
                                    }
                                    else -> {
                                        requestPermissionLauncher.launch(galleryPermissions)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.camera),
                                contentDescription = "like",
                                tint = CustomTheme.colors.iconDefault,
                            )
                        }
                    }
                }
            }
            item{
                Column{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 6.dp),
                            text = "재료 \uD83D\uDCCC",
                            style = CustomTheme.typography.title1,
                            color = CustomTheme.colors.textPrimary,
                        )
                        IconButton(
                            onClick = {
                                ingredients.add(mutableStateOf(""))
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add),
                                contentDescription = "add",
                                tint = CustomTheme.colors.iconDefault,
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.borderLight
                    )
                }
            }
            items(ingredients.size){ index ->
                val i = ingredients[index]
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✅",
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textPrimary,
                    )
                    TextField(
                        value = i.value,
                        onValueChange = { ingredients[index].value = it },
                        placeholder = {
                            Text(
                                text = "재료 추가..//",
                                style = CustomTheme.typography.body3,
                                color = CustomTheme.colors.textSecondary
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    ingredients[index].value = ""
                                }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.delete),
                                    contentDescription = "delete",
                                    tint = CustomTheme.colors.iconDefault,
                                )
                            }
                        },
                        textStyle = CustomTheme.typography.body2,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = CustomTheme.colors.textPrimary,
                            unfocusedTextColor = CustomTheme.colors.textPrimary,
                            focusedContainerColor = CustomTheme.colors.onSurface,
                            unfocusedContainerColor = CustomTheme.colors.onSurface,
                            cursorColor = CustomTheme.colors.textPrimary,
                            focusedIndicatorColor = CustomTheme.colors.border,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTrailingIconColor = CustomTheme.colors.iconDefault,
                            unfocusedTrailingIconColor = Color.Transparent,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                    IconButton(
                        onClick = {
                            ingredients.removeAt(index)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            contentDescription = "delete",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            }
            item{
                Column{
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "레시피 \uD83D\uDE80",
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.borderLight
                    )
                }
            }
            item{
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = instructions,
                    onValueChange = { instructions = it },
                    placeholder = {
                        Text(
                            text = "레시피..//",
                            style = CustomTheme.typography.body3,
                            color = CustomTheme.colors.textSecondary
                        )
                    },
                    textStyle = CustomTheme.typography.body2,
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
            item{
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
                    onClick = {
                        onEvent(HomeEvent.ModifyRecipe(
                            Recipe(
                                id = recipe.id,
                                title = title,
                                imageUrl = image,
                                ingredients = ingredients.map { it.value.toString() }.toList(),
                                instructions = instructions,
                                liked = recipe.liked
                            )
                        ))
                    }
                ) {
                    Text(
                        text = "수정하기",
                        style = CustomTheme.typography.button1,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RecipeModifyScreenPreview() {
    val recipe = Recipe(
        id = 1,
        title = "레시피 제목",
        imageUrl = null,
        ingredients = listOf("재료1", "재료2", "재료3"),
        instructions = "1. 레시피 설명1 2. 레시피 설명2 3. 레시피 설명3",
        liked = false,
    )
    RecipeModifyScreen(recipe, ModifyState(
    ), {}, {})
}