package com.warchaser.libbase.network.bean.coroutine

import com.warchaser.libcommonutils.NLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

private val TAG : String = "LibBase.TAG"

sealed class Result<out T : Any>{

    var isSuccess = false

    data class Success<out T : Any>(val body : T?) : Result<T>()
    data class Error(val msg : String) : Result<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Request result is $body"
            is Error -> "Request error, msg is $msg"
        }
    }

}

suspend fun <T : Any> Result<T>.onSuccess(successBlock: (suspend CoroutineScope.(T) -> Unit)? = null) : Result<T> = coroutineScope {
    if(isSuccess){
        NLog.e(TAG, "onSuccess current coroutineContext is $coroutineContext")
        (this@onSuccess as Result.Success<*>).body?.run {
            successBlock?.invoke(this@coroutineScope, this as T)
        }
    }
    this@onSuccess
}

suspend fun <T : Any> Result<T>.onError(errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null) : Result<T> = coroutineScope {
    if(!isSuccess){
        NLog.e(TAG, "onError current coroutineContext is $coroutineContext")
        errorBlock?.invoke(this, (this@onError as Result.Error).msg)
    }
    this@onError
}