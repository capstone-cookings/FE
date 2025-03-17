package com.example.untitled_capstone.di

import android.content.Context
import androidx.room.Room
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.remote.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFridgeItemDatabase(@ApplicationContext context: Context): FridgeItemDatabase{
        return Room.databaseBuilder(
            context, FridgeItemDatabase::class.java, "fridge_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideDao(db: FridgeItemDatabase): FridgeItemDao = db.dao

}