package com.example.untitled_capstone.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.untitled_capstone.core.util.Constants.BASE_URL
import com.example.untitled_capstone.core.util.Constants.KAKAO_BASE_URL
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.db.LikedPostDatabase
import com.example.untitled_capstone.data.local.db.MessageItemDatabase
import com.example.untitled_capstone.data.local.db.MyPostDatabase
import com.example.untitled_capstone.data.local.db.NotificationDatabase
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.db.ProfileDatabase
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.local.entity.LikedPostEntity
import com.example.untitled_capstone.data.local.entity.MessageItemEntity
import com.example.untitled_capstone.data.local.entity.MyPostEntity
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.local.remote.LikedPostDao
import com.example.untitled_capstone.data.local.remote.MessageDao
import com.example.untitled_capstone.data.local.remote.MyPostDao
import com.example.untitled_capstone.data.local.remote.NotificationDao
import com.example.untitled_capstone.data.local.remote.PostItemDao
import com.example.untitled_capstone.data.local.remote.ProfileDao
import com.example.untitled_capstone.data.local.remote.RecipeItemDao
import com.example.untitled_capstone.data.pagination.FridgePagingSource
import com.example.untitled_capstone.data.pagination.LikedPostPagingSource
import com.example.untitled_capstone.data.pagination.MessagePagingSource
import com.example.untitled_capstone.data.pagination.MyPostPagingSource
import com.example.untitled_capstone.data.pagination.PostPagingSource
import com.example.untitled_capstone.data.pagination.RecipePagingSource
import com.example.untitled_capstone.data.remote.manager.WebSocketManager
import com.example.untitled_capstone.data.remote.service.ChatApi
import com.example.untitled_capstone.data.remote.service.FcmApi
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.remote.service.HomeApi
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MapApi
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.data.remote.service.TokenApi
import com.example.untitled_capstone.data.util.ApiResponseAdapterFactory
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.token.AuthAuthenticator
import com.example.untitled_capstone.domain.use_case.token.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipeItemDatabase(@ApplicationContext context: Context): RecipeItemDatabase{
        return Room.databaseBuilder(
            context, RecipeItemDatabase::class.java, "recipe_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: RecipeItemDatabase): RecipeItemDao = db.dao

    @Provides
    @Singleton
    fun provideFridgeItemDatabase(@ApplicationContext context: Context): FridgeItemDatabase{
        return Room.databaseBuilder(
            context, FridgeItemDatabase::class.java, "fridge_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFridgeDao(db: FridgeItemDatabase): FridgeItemDao = db.dao

    @Provides
    @Singleton
    fun providePostItemDatabase(@ApplicationContext context: Context): PostItemDatabase{
        return Room.databaseBuilder(
            context, PostItemDatabase::class.java, "post_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePostDao(db: PostItemDatabase): PostItemDao = db.dao

    @Provides
    @Singleton
    fun provideLikedPostItemDatabase(@ApplicationContext context: Context): LikedPostDatabase {
        return Room.databaseBuilder(
            context, LikedPostDatabase::class.java, "liked_post__database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLikedPostDao(db: LikedPostDatabase): LikedPostDao = db.dao

    @Provides
    @Singleton
    fun provideMyPostItemDatabase(@ApplicationContext context: Context): MyPostDatabase {
        return Room.databaseBuilder(
            context, MyPostDatabase::class.java, "my_post_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMyPostDao(db: MyPostDatabase): MyPostDao = db.dao

    @Provides
    @Singleton
    fun provideMessageItemDatabase(@ApplicationContext context: Context): MessageItemDatabase{
        return Room.databaseBuilder(
            context, MessageItemDatabase::class.java, "message_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMessageDao(db: MessageItemDatabase): MessageDao = db.dao

    @Provides
    @Singleton
    fun provideProfileDatabase(@ApplicationContext context: Context): ProfileDatabase{
        return Room.databaseBuilder(
            context, ProfileDatabase::class.java, "profile_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(db: ProfileDatabase): ProfileDao = db.dao

    @Provides
    @Singleton
    fun provideNotificationDatabase(@ApplicationContext context: Context): NotificationDatabase {
        return Room.databaseBuilder(
            context, NotificationDatabase::class.java, "notification_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(db: NotificationDatabase): NotificationDao = db.dao

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenRepository: TokenRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository))
            .authenticator(AuthAuthenticator(tokenRepository))
            .build()
    }

    @Provides
    @Singleton
    fun provideFridgeApi(gson: Gson, okHttpClient: OkHttpClient): FridgeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FridgeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApi(gson: Gson): LoginApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyApi(gson: Gson, okHttpClient: OkHttpClient): MyApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(gson: Gson, okHttpClient: OkHttpClient): HomeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApi(gson: Gson, okHttpClient: OkHttpClient): PostApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMapApi(): MapApi {
        return Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(gson: Gson): TokenApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(gson: Gson, okHttpClient: OkHttpClient): ChatApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmApi(gson: Gson, okHttpClient: OkHttpClient): FcmApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FcmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(ApiResponseAdapterFactory())
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideFridgePager(db: FridgeItemDatabase, api: FridgeApi, fetchType: FridgeFetchType): Pager<Int, FridgeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = FridgePagingSource(
                db = db,
                api = api,
                fetchType = fetchType
            ),
            pagingSourceFactory = {
                db.dao.getFridgeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideRecipePager(db: RecipeItemDatabase, api: HomeApi): Pager<Int, RecipeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RecipePagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getRecipeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePostPager(db: PostItemDatabase, api: PostApi): Pager<Int, PostItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLikedPostPager(db: LikedPostDatabase, api: PostApi): Pager<Int, LikedPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = LikedPostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMyPostPager(db: MyPostDatabase, api: PostApi): Pager<Int, MyPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MyPostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMessagePager(roomId: Long, db: MessageItemDatabase, api: ChatApi): Pager<Int, MessageItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MessagePagingSource(
                roomId = roomId,
                db = db,
                api = api,
            ),
            pagingSourceFactory = {
                db.dao.getMessagesPaging(roomId)
            }
        )
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(): WebSocketManager = WebSocketManager()
}