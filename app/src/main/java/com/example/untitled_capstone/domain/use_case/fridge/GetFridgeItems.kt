package com.example.untitled_capstone.domain.use_case.fridge

import androidx.paging.PagingData
import androidx.paging.map
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFridgeItems @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(fetchType: FridgeFetchType): Flow<PagingData<FridgeItem>> {
        return fridgeRepository.getFridgeItemsPaged(fetchType).map { pagingData ->
            pagingData.map { it.toFridgeItem() }
        }
    }
}
