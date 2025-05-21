package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportUserUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(targetUserId: Long, reportType: String, content: String): Flow<Resource<String>> {
        return myRepository.repostUser(targetUserId, reportType, content)
    }
}