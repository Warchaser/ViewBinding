package com.warchaser.viewbinding.home.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.warchaser.libbase.network.bean.coroutine.Result
import com.warchaser.libbase.ui.BaseViewModel
import com.warchaser.libcommonutils.NLog
import com.warchaser.viewbinding.home.repository.MVVMDemoRepository
import com.warchaser.viewbinding.home.state.MVVMDemoUIState
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MVVMDemoViewModel : BaseViewModel(){

    private val repository = MVVMDemoRepository()

    private val _uiState = MutableLiveData<MVVMDemoUIState<Body<VIN>>>()

    val vinState = UnPeekLiveData<VIN>()
    var accessToken = ObservableField<String>()

    val uiState : LiveData<MVVMDemoUIState<Body<VIN>>>
        get() = _uiState

    fun getVIN(){
        viewModelScope.launch(Dispatchers.Main){
            NLog.e(TAG, "getVIN")
            repository.getVIN().collect<Result<Body<VIN>>> {
                if(it.isSuccess){
                    val success = ((it as Result.Success<Body<VIN>>).body)?.responseBody
                    success?.run {
                        this@MVVMDemoViewModel.accessToken.set(accessToken)
//                        vinState.postValue(this)
                    }
                } else {
                    val error = it as Result.Error
                    this@MVVMDemoViewModel.accessToken.set(error.msg)
//                    vinState.postValue(VIN(error.msg, "", ""))
                }
            }
        }
    }

}