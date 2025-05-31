package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val repository: HomeRepository
)  {
    operator fun invoke(recipe: String): Flow<Resource<String>>{
        return repository.addRecipe(recipe)
    }
}