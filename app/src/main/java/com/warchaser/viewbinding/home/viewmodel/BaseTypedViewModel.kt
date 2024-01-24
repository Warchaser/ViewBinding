package com.warchaser.viewbinding.home.viewmodel

import com.warchaser.libbase.ui.BaseViewModel
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.libbase.network.bean.coroutine.Result

abstract class BaseTypedViewModel : BaseViewModel() {

    fun <T> getResponseBody(result: Result<Body<T>>) = ((result as Result.Success<Body<T>>).body)?.responseBody

    fun <T> getError(result: Result<Body<T>>) = result as Result.Error

    fun <T> getResponseBody(body : Body<T>) : T? = body.responseBody
}