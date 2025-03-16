package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import javax.inject.Inject

class ModifyFridgeItems @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    suspend operator fun invoke(item: FridgeItem) {
        val dto = item.toContentDto()
        fridgeRepository.modifyItem(dto)
    }
}