package com.warchaser.viewbinding.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
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

    val uiState : LiveData<MVVMDemoUIState<Body<VIN>>>
        get() = _uiState

    fun getVIN(){
        viewModelScope.launch(Dispatchers.Main){
            NLog.e("MVVMDemoViewModel", "getVIN")
            repository.getVIN().collect {
                _uiState.postValue(it)
            }
        }
    }

}