package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadPostImagesUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(id: Long, images: List<File>): Flow<Resource<String>> {
        return repository.uploadImages(id, images)
    }
}