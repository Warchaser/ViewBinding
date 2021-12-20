package com.warchaser.libbase.network.bean.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

data class Response<out T>(val errorCode : Int, val errorMsg: String, val body: T)

suspend fun <T : Any> Response<T>.executeResponse(successBlock : (suspend CoroutineScope.() -> Unit)? = null, errorBlock: (suspend CoroutineScope.() -> Unit)? = null) : Result<T> = coroutineScope {
    if(errorCode == -1){
        errorBlock?.let { it() }
        Result.Error(errorMsg)
    } else {
        successBlock?.let { it() }
        Result.Success(body)
    }
}


suspend fun <T : Any> Response<T>.onResponse(responseBlock: (suspend CoroutineScope.(T) -> Unit)? = null) : Response<T> = coroutineScope {
    if(errorCode != -1){
        responseBlock?.invoke(this, this@onResponse.body)
    }
    this@onResponse
}



suspend fun <T : Any> Response<T>.onError(errorBlock : (suspend CoroutineScope.(String) -> Unit)? = null) : Response<T> = coroutineScope {
    if(errorCode == -1){
        errorBlock?.invoke(this, this@onError.errorMsg)
    }
    this@onError
}
