package com.warchaser.libbase.network.bean.service.coroutine

import com.warchaser.libbase.network.bean.coroutine.CODE_SUCCESS
import retrofit2.Response
import com.warchaser.libbase.network.bean.coroutine.Result
import com.warchaser.libcommonutils.NLog
import com.warchaser.libcommonutils.PackageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

open class BaseRepository{

    protected val TAG : String by lazy {
        PackageUtil.getSimpleClassName(this)
    }

    suspend fun <T : Any> apiCall(call : suspend () -> Response<T>) : Response<T> = call.invoke()

    suspend fun <T : Any> safeApiCall(call : suspend () -> Result<T>, errorMsg : String) : Result<T> = try {
        call()
    } catch (e : Exception){
        Result.Error(errorMsg)
    }

    suspend fun <T : Any> safeApiCall(call : suspend () -> Result<T>) : Result<T> = try {
        call()
    } catch (e : Throwable) {
        Result.Error(e.message!!)
    }

    suspend fun <T : Any> safeApiCallResponse(call : suspend () -> Response<T>) : Flow<Result<T>> = flow{
        try {
            val response = call.invoke()
            response.run {
                if(code() != CODE_SUCCESS){
                    emit(Result.Error(message()))
                } else {
                    val result = Result.Success(body())
                    result.isSuccess = true
                    emit(result)
                }
            }
        } catch (e : Throwable) {
            emit(Result.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun <T : Any> safeApiCallArgus(call : suspend () -> Response<T>, successBlock: (suspend CoroutineScope.(Result.Success<T>) -> Unit)? = null, errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null) : Result<T> = coroutineScope{
        try {
            val response = call.invoke()
            response.run {
                if(code() != CODE_SUCCESS){
                    errorBlock?.invoke(this@coroutineScope, message())
                    Result.Error(message())
                } else {
                    val result = Result.Success(body())
                    result.isSuccess = true
                    successBlock?.invoke(this@coroutineScope, result)
                    result
                }
            }
        } catch (e : Throwable) {
            val msg = e.toString()
            errorBlock?.invoke(this@coroutineScope, msg)
            Result.Error(msg)
        }
    }

    suspend fun <T : Any> safeApiCallArgus(call : suspend () -> Response<T>) : Result<T> = withContext(Dispatchers.IO){
        var responseResult : Result<T>?
        val duration = measureTimeMillis {
            NLog.e(TAG, "safeApiCallArgus current coroutineContext is $coroutineContext")
            responseResult = try {
                val response = call.invoke()
                response.run {
                    if(code() != CODE_SUCCESS){
                        Result.Error(message())
                    } else {
                        val result = Result.Success(body())
                        result.isSuccess = true
                        result
                    }
                }
            } catch (e : Throwable) {
                val msg = e.toString()
                Result.Error(msg)
            }
        }

        NLog.e(TAG, "$call run with $duration ms")

        responseResult!!

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