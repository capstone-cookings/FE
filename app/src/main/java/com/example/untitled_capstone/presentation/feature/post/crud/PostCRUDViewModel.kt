package com.example.untitled_capstone.presentation.feature.post.crud

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.use_case.post.AddPostUseCase
import com.example.untitled_capstone.domain.use_case.post.DeletePostImageUseCase
import com.example.untitled_capstone.domain.use_case.post.ModifyPostUseCase
import com.example.untitled_capstone.domain.use_case.post.UploadPostImagesUseCase
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostCRUDViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val modifyPostUseCase: ModifyPostUseCase,
    private val uploadPostImagesUseCase: UploadPostImagesUseCase,
    private val deletePostImageUseCase: DeletePostImageUseCase
): ViewModel()  {
    val uiState: MutableStateFlow<PostCRUDUiState> = MutableStateFlow<PostCRUDUiState>(PostCRUDUiState.Idle)

    private val _event = MutableSharedFlow<PostEvent>()
    val event = _event.asSharedFlow()


    fun addNewPost(post: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            addPostUseCase(post, images).collectLatest{
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(PostCRUDUiState.Success)
                        _event.emit(PostEvent.ClearBackStack)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostCRUDUiState.Error(it.message))
                        _event.emit(PostEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun modifyPost(id: Long, newPost: NewPost, images: List<File>? = null){
        viewModelScope.launch {
            if(images != null){
                uploadPostImagesUseCase(id, images).collectLatest{
                    when(it){
                        is Resource.Success -> {
                            uiState.tryEmit(PostCRUDUiState.Success)
                            _event.emit(PostEvent.ClearBackStack)
                        }
                        is Resource.Error -> {
                            uiState.tryEmit(PostCRUDUiState.Error(it.message))
                            _event.emit(PostEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Loading -> {
                            uiState.tryEmit(PostCRUDUiState.Loading)
                        }
                    }
                }
                modifyPostUseCase(id, newPost).collectLatest{
                    when(it){
                        is Resource.Success -> {
                            uiState.tryEmit(PostCRUDUiState.Success)
                        }
                        is Resource.Error -> {
                            uiState.tryEmit(PostCRUDUiState.Error(it.message))
                            _event.emit(PostEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Loading -> {
                            uiState.tryEmit(PostCRUDUiState.Loading)
                        }
                    }
                }
            } else {
                modifyPostUseCase(id, newPost).collectLatest{
                    when(it){
                        is Resource.Success -> {
                            uiState.tryEmit(PostCRUDUiState.Success)
                            _event.emit(PostEvent.ClearBackStack)
                        }
                        is Resource.Error -> {
                            uiState.tryEmit(PostCRUDUiState.Error(it.message))
                            _event.emit(PostEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Loading -> {
                            uiState.tryEmit(PostCRUDUiState.Loading)
                        }
                    }
                }
            }
        }
    }

    fun deletePostImage(id: Long, imageId: Long){
        viewModelScope.launch {
            deletePostImageUseCase(id, imageId).collectLatest{
                when(it){
                    is Resource.Success -> {
                        uiState.tryEmit(PostCRUDUiState.Idle)
                    }
                    is Resource.Error -> {
                        uiState.tryEmit(PostCRUDUiState.Error(it.message))
                        _event.emit(PostEvent.ShowSnackbar(it.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        uiState.tryEmit(PostCRUDUiState.Loading)
                    }
                }
            }
        }
    }

    fun navigateUp(route: Screen) {
        viewModelScope.launch {
            _event.emit(PostEvent.Navigate(route))
        }
    }

    fun popBackStack() {
        viewModelScope.launch {
            _event.emit(PostEvent.PopBackStack)
        }
    }
}

@Stable
interface PostCRUDUiState{
    data object Idle: PostCRUDUiState
    data object Success: PostCRUDUiState
    data object Loading: PostCRUDUiState
    data class Error(val message: String?): PostCRUDUiState
}