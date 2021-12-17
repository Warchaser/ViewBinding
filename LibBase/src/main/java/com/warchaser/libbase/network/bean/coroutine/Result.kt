package com.warchaser.libbase.network.bean.coroutine

sealed class Result<out T : Any>{

    data class Success<out T : Any>(val body : T) : Result<T>()
    data class Error(val msg : String) : Result<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Request result is $body"
            is Error -> "Request error, msg is $msg"
        }
    }

}