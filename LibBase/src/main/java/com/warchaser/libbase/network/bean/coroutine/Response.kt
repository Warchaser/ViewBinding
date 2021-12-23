package com.warchaser.libbase.network.bean.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

private const val CODE_SUCCESS = 200

//data class Response<out T>(val code : String, val responseBody : T, val description : String)

//suspend fun <T : Any> Response<T>.executeResponse(successBlock : (suspend CoroutineScope.() -> Unit)? = null, errorBlock: (suspend CoroutineScope.() -> Unit)? = null) : Result<T> = coroutineScope {
//    if(code == -1){
//        errorBlock?.let { it() }
//        Result.Error(errorMsg)
//    } else {
//        successBlock?.let { it() }
//        Result.Success(body)
//    }
//}


suspend fun <T : Any> Response<T>.onResponse(responseBlock: (suspend CoroutineScope.(T) -> Unit)? = null) : Response<T> = coroutineScope {
    if(this@onResponse.code() == CODE_SUCCESS){
        this@onResponse.body()?.run {
            responseBlock?.invoke(this@coroutineScope, this)
        }
    }
    this@onResponse
}



suspend fun <T : Any> Response<T>.onError(errorBlock : (suspend CoroutineScope.(String) -> Unit)? = null) : Response<T> = coroutineScope {
    if(this@onError.code() != CODE_SUCCESS){
        errorBlock?.invoke(this, this@onError.message())
    }
    this@onError
}
