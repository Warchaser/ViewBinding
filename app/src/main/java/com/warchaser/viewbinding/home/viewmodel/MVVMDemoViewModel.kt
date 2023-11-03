package com.warchaser.viewbinding.home.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.warchaser.libbase.network.bean.coroutine.onError
import com.warchaser.libbase.network.bean.coroutine.onSuccess
import com.warchaser.libcommonutils.NLog
import com.warchaser.viewbinding.home.repository.MVVMDemoRepository
import com.warchaser.viewbinding.home.state.MVVMDemoUIState
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MVVMDemoViewModel : BaseTypedViewModel(){

    private val repository = MVVMDemoRepository()

    private val _uiState = MutableLiveData<MVVMDemoUIState<Body<VIN>>>()

    val vinState = UnPeekLiveData<VIN>()
    var accessToken = ObservableField<String>()

    val uiState : LiveData<MVVMDemoUIState<Body<VIN>>>
        get() = _uiState

    fun getVIN(){
        viewModelScope.launch(Dispatchers.Main){
            NLog.e(TAG, "getVIN")
//            repository.getVINArgus(
//                successBlock = {
//                    success ->
//                        getResponseBody(success)?.run {
//                            this@MVVMDemoViewModel.accessToken.set(accessToken)
//                        }
//
//                },
//                errorBlock = {
//                    error ->
//                        accessToken.set(error)
//                }
//            )

            repository.getVINArgus1()
                .onSuccess {
                    response ->
                        getResponseBody(response).run {
                            this@MVVMDemoViewModel.accessToken.set(accessToken)
                        }
                }
                .onError {
                    error ->
                        accessToken.set(error)
                }
//            repository.getVIN().collect {
//                if(it.isSuccess){
//                    val success = getResponseBody(it)
//                    success?.run {
//                        this@MVVMDemoViewModel.accessToken.set(accessToken)
////                        vinState.postValue(this)
//                    }
//                } else {
//                    val error = getError(it)
//                    this@MVVMDemoViewModel.accessToken.set(error.msg)
////                    vinState.postValue(VIN(error.msg, "", ""))
//                }
//            }
        }
    }

}