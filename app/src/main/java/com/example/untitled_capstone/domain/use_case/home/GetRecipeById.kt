package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.repository.HomeRepository
import javax.inject.Inject

class GetRecipeById @Inject constructor(
    private val repository: HomeRepository
)  {
    suspend operator fun invoke(id: Long): Resource<Recipe>{
        return repository.getRecipeById(id)
    }
}