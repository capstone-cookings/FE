package com.stone.fridge.domain.use_case.token

import com.stone.fridge.domain.repository.TokenRepository
import com.kakao.sdk.common.Constants.AUTHORIZATION
import com.stone.fridge.presentation.util.AuthEvent
import com.stone.fridge.presentation.util.AuthEventBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_RETRY) {
            AuthEventBus.send(AuthEvent.Logout)
            return null // 무한 루프 방지
        }

        if (response.code != 401) return null

        val newToken = runBlocking {
            withContext(Dispatchers.IO) {
                tokenManager.refreshAndSaveToken()
            }
        } ?: return null

        AuthEventBus.send(AuthEvent.Login)

        return response.request.newBuilder()
            .header(AUTHORIZATION, newToken.accessToken)
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    companion object {
        private const val MAX_AUTH_RETRY = 3
    }
}