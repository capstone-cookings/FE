package com.example.untitled_capstone.presentation.feature.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.ui.theme.CustomTheme
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(recipe: Recipe, navController: NavHostController){
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(Dimens.topBarPadding),
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()}
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
                        text = recipe.title,
                        style = CustomTheme.typography.title1,
                        color = CustomTheme.colors.textPrimary,
                        softWrap = true
                    )
                },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.more),
                            tint = CustomTheme.colors.iconDefault,
                            contentDescription = "more",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .padding(horizontal = Dimens.surfaceHorizontalPadding,
                    vertical = Dimens.surfaceVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(300.dp)
                    .clip(shape = RoundedCornerShape(Dimens.cornerRadius)),
                contentAlignment = Alignment.BottomEnd
            ){
                if (recipe.image != null) {
                    Image(
                        painter = painterResource(recipe.image),
                        contentDescription = recipe.title,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(300.dp)
                            .clip(shape = RoundedCornerShape(Dimens.cornerRadius))
                    )
                }
                Row {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.camera),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.heart),
                            contentDescription = "like",
                            tint = CustomTheme.colors.iconDefault,
                        )
                    }
                }
            }
            Text(
                text = "재료",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            Column {
                for(ingredient in recipe.ingredients){
                    Text(
                        text = ingredient,
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textPrimary
                    )
                }
            }
            Text(
                text = "레시피",
                style = CustomTheme.typography.title1,
                color = CustomTheme.colors.textPrimary,
            )
            Column {
                for(step in recipe.steps){
                    Text(
                        text = step,
                        style = CustomTheme.typography.body2,
                        color = CustomTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}