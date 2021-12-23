package com.warchaser.libbase.network.bean.service.coroutine

import com.warchaser.libbase.network.bean.coroutine.CODE_SUCCESS
import retrofit2.Response
import com.warchaser.libbase.network.bean.coroutine.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository{

    suspend fun <T : Any> apiCall(call : suspend () -> Response<T>) : Response<T> = call.invoke()

    suspend fun <T : Any> safeApiCall(call : suspend () -> Result<T>, errorMsg : String) : Result<T> = try {
        call()
    } catch (e : Exception){
        Result.Error(errorMsg)
    }

    suspend fun <T : Any> executeResponse(response: Response<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null, errorBlock: (suspend  CoroutineScope.() -> Unit)? = null): Result<T> = coroutineScope{
        if(response.code() == CODE_SUCCESS){
            errorBlock?.let { it() }
            Result.Error(response.message())
        } else {
            successBlock?.let { it() }
            Result.Success(response.body())
        }
    }

    suspend fun <T : Any> executeResponseFlow(response : Response<T>): Flow<Result<T>> = flow {
        response.run {
            if(code() == CODE_SUCCESS){
                emit(Result.Error(message()))
            } else {
                emit(Result.Success(body()))
            }
        }
    }
    .flowOn(Dispatchers.IO)
    .catch {
        emit(Result.Error("Network Error"))
    }

}