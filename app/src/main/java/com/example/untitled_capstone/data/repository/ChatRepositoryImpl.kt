package com.example.untitled_capstone.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Constants.MESSAGE_PAGE_SIZE
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.db.MessageItemDatabase
import com.example.untitled_capstone.data.local.entity.MessageItemEntity
import com.example.untitled_capstone.data.pagination.MessagePagingSource
import com.example.untitled_capstone.data.remote.service.ChatApi
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
    private val db: MessageItemDatabase,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): ChatRepository {

    @WorkerThread
    override suspend fun readChats(id: Long): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readChats(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun joinChatRoom(id: Long): Flow<Resource<ChattingRoom>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.joinChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toChattingRoom()))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun closeChatRoom(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.closeChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun enterChatRoom(id: Long): Flow<Resource<ChattingRoom>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.enterChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toChattingRoom()))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun checkWhoIsIn(id: Long): Flow<Resource<List<ChatMember>>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.checkWhoIsIn(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toChatMember() }))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun getMessages(id: Long, lastMessageId: Long?): Flow<Resource<List<Message>>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.getMessages(id, lastMessageId)
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toMessage() }))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun getMyRooms(): Flow<Resource<List<ChattingRoomRaw>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getMyRooms()
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toChattingRoomRaw() }))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun exitChatRoom(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.exitChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getMessagePaged(roomId: Long): Flow<PagingData<MessageItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = MESSAGE_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MessagePagingSource(roomId, api, db),
            pagingSourceFactory = { db.dao.getMessagesPaging(roomId) }
        ).flow.flowOn(ioDispatcher)
    }
}