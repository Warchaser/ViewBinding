package com.warchaser.libbase.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel(){

    fun launchOnUI(block : suspend CoroutineScope.() -> Unit){
        viewModelScope.launch { block() }
    }

    suspend fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO){
            block
        }
    }

    open class UIState<T>(
        val isLoading : Boolean = false,
        val isRefresh : Boolean = false,
        val isSuccess : T? = null,
        val isError : String? = null,
        val isEmpty : Boolean = false
    )

    fun <T> BaseViewModel.launch(block: () -> T, success: (T) -> Unit, error: (Throwable) -> Unit = {}){
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO){
                    block()
                }
            }.onSuccess {
                success(it)
            }.onFailure { err ->
                error(err)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}