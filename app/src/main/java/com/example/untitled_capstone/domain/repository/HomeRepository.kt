package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PreferenceDto
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.domain.model.TastePreference
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getTastePreference(): Resource<TastePreference>
    suspend fun setTastePreference(tastePreference: PreferenceDto): Resource<ApiResponse>
    fun getRecipes(): Flow<PagingData<RecipeItemEntity>>
    suspend fun getRecipeById(id: Long): Resource<RecipeRaw>
    suspend fun toggleLike(id: Long, liked: Boolean): Resource<ApiResponse>
    suspend fun addRecipe(title: String, instructions: String): Resource<ApiResponse>
    suspend fun getFirstRecommendation(): Resource<String>
    suspend fun getAnotherRecommendation(): Resource<String>
    fun isFirstSelection(): Boolean
    fun setFirstSelection(isFirst: Boolean)
}