package com.example.untitled_capstone.presentation.feature.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeUseCases: FridgeUseCases
): ViewModel() {
    private val _state = MutableStateFlow(FridgeState())
    val state = _state.asStateFlow()

    private val _pagingDataFlow = MutableStateFlow<PagingData<FridgeItem>>(PagingData.empty())
    val pagingData: StateFlow<PagingData<FridgeItem>> = _pagingDataFlow.asStateFlow()

    init {
        onAction(FridgeAction.GetItems)
    }

    fun onAction(action: FridgeAction){
        when(action){
            is FridgeAction.GetItems -> getItems()
            is FridgeAction.AddItem -> addItem(action.item)
            is FridgeAction.GetItemsByDate -> getItemsByDate()
            is FridgeAction.ToggleNotification -> toggleNotification(action.id, action.alarmStatus)
            is FridgeAction.ModifyItem -> modifyItem(action.item)
            is FridgeAction.DeleteItem -> deleteItem(action.id)
            is FridgeAction.GetItemById -> getItemById(action.id)
        }
    }

    private fun addItem(item: FridgeItem){
        viewModelScope.launch {
            val result = fridgeUseCases.addFridgeItem(item)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun toggleNotification(id: Long, alarmStatus: Boolean) {
        viewModelScope.launch {
            val result = fridgeUseCases.toggleNotification(id, alarmStatus)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun modifyItem(updatedItem: FridgeItem) {
        viewModelScope.launch {
            val result = fridgeUseCases.modifyFridgeItems(updatedItem)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun deleteItem(id: Long) {
        viewModelScope.launch {
            val result = fridgeUseCases.deleteFridgeItem(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun getItemById(id: Long) {
        viewModelScope.launch {
            val result = fridgeUseCases.getFridgeItemById(id)
            when(result){
                is Resource.Success -> {
                    result.data?.let{
                        _state.update { it.copy(
                            item = result.data,
                            loading = false,
                            error = null) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message ?: "An unexpected error occurred") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(loading = true) }
                }
            }
        }
    }

    private fun getItems() {
        viewModelScope.launch {
            fridgeUseCases.getFridgeItems()
                .cachedIn(viewModelScope).collect {
                    _pagingDataFlow.value = it
                }
        }
    }

    private fun getItemsByDate() {
        viewModelScope.launch {
            fridgeUseCases.getFridgeItemsByDate()
                .cachedIn(viewModelScope)
        }
    }
}