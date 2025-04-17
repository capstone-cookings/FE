package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.domain.model.FridgeItem

data class ContentDto(
    val alarmStatus: Boolean,
    val count: Int,
    val imageUrl: String?,
    val foodName: String,
    val id: Long,
    val storageType: Boolean,
    val useByDate: Long
){
    fun toFridgeItemEntity(pagerNumber: Int): FridgeItemEntity {
        return FridgeItemEntity(
            id = id,
            name = foodName,
            image = imageUrl,
            quantity = count.toString(),
            expirationDate = useByDate,
            notification = alarmStatus,
            isFridge = storageType,
            pagerNumber = pagerNumber
        )
    }
    fun toFridgeItem(): FridgeItem {
        return  FridgeItem(
            id = id,
            name = foodName,
            image = imageUrl,
            quantity = count.toString(),
            expirationDate = useByDate,
            notification = alarmStatus,
            isFridge = storageType,
        )
    }
}