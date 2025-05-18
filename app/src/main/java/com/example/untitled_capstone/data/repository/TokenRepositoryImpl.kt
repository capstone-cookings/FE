package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.untitled_capstone.core.util.Crypto
import com.example.untitled_capstone.core.util.PrefKeys.ACCESS_TOKEN_KEY
import com.example.untitled_capstone.core.util.PrefKeys.REFRESH_TOKEN_KEY
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.TokenDto
import com.example.untitled_capstone.data.remote.service.TokenApi
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val api: TokenApi
): TokenRepository {

    val dataStore = context.dataStore


    override suspend fun getAccessToken(): String? {
        val base64 = dataStore.data
            .map { prefs -> prefs[ACCESS_TOKEN_KEY] ?: return@map null }
            .first() ?: return null

        val encryptedBytes = Base64.decode(base64, Base64.DEFAULT)
        val decryptedBytes = Crypto.decrypt(encryptedBytes)
        return String(decryptedBytes)
    }

    override suspend fun getRefreshToken(): String? {
        val base64 = dataStore.data
            .map { prefs -> prefs[REFRESH_TOKEN_KEY] ?: return@map null }
            .first() ?: return null

        val encryptedBytes = Base64.decode(base64, Base64.DEFAULT)
        val decryptedBytes = Crypto.decrypt(encryptedBytes)
        return String(decryptedBytes)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        val encryptedBytes = Crypto.encrypt(accessToken.toByteArray())
        val base64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = base64
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        val encryptedBytes = Crypto.encrypt(refreshToken.toByteArray())
        val base64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = base64
        }
    }

    override suspend fun deleteTokens(){
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Resource<ApiResponse<TokenDto>> {
        return try {
            Resource.Loading(data = null)
            val response = api.refreshToken(refreshToken)
            if(response.isSuccess){
                Resource.Success(response)
            }else{
                Log.d("response error", response.toString())
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_prefs")