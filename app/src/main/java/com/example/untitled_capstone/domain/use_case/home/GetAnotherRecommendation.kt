package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.HomeRepository
import javax.inject.Inject

class GetAnotherRecommendation @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Resource<String> {
        return repository.getAnotherRecommendation()
    }
}