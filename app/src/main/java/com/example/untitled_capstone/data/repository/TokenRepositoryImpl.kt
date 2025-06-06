package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.untitled_capstone.BuildConfig
import com.example.untitled_capstone.core.util.PrefKeys.ACCESS_TOKEN_KEY
import com.example.untitled_capstone.core.util.PrefKeys.REFRESH_TOKEN_KEY
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.TokenDto
import com.example.untitled_capstone.data.remote.service.TokenApi
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val api: TokenApi
): TokenRepository {

    val dataStore = context.dataStore


    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.APPLICATION_ID)