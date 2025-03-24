package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.TastePreference
import com.example.untitled_capstone.domain.repository.HomeRepository
import javax.inject.Inject


class SetTastePreference @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(tastePreference: TastePreference): Resource<ApiResponse> {
        return repository.setTastePreference(tastePreference.toPreferenceDto())
    }
}