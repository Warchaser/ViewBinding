package com.warchaser.libbase.network.bean.coroutine

import com.warchaser.libcommonutils.NLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

const val CODE_SUCCESS = 200
private const val TAG = "NetworkResponse"

suspend fun <T : Any> Response<T>.executeResponse(successBlock : (suspend CoroutineScope.() -> Unit)? = null, errorBlock: (suspend CoroutineScope.() -> Unit)? = null) : Result<T> = coroutineScope {

    if(this@executeResponse.code() != CODE_SUCCESS){
        errorBlock?.let { it() }
        Result.Error(this@executeResponse.message())
    } else {
        successBlock?.let { it() }
        Result.Success(this@executeResponse.body())
    }
}

suspend fun <T : Any> Response<T>.onResponse(responseBlock: (suspend CoroutineScope.(T) -> Unit)? = null) : Response<T> = coroutineScope {
    if(this@onResponse.code() == CODE_SUCCESS){
        NLog.d(TAG, "onResponse")
        this@onResponse.body()?.run {
            responseBlock?.invoke(this@coroutineScope, this)
        }
    }
    this@onResponse
}



suspend fun <T : Any> Response<T>.onError(errorBlock : (suspend CoroutineScope.(String) -> Unit)? = null) : Response<T> = coroutineScope {
    if(this@onError.code() != CODE_SUCCESS){
        NLog.d(TAG, "onError")
        errorBlock?.invoke(this, this@onError.message())
    }
    this@onError
}
