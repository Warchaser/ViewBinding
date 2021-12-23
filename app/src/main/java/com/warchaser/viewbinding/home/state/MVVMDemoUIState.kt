package com.warchaser.viewbinding.home.state

import com.warchaser.libbase.ui.BaseViewModel

class MVVMDemoUIState<T>(
    isLoading: Boolean = false,
    isSuccess: T? = null,
    isError: String? = null) : BaseViewModel.UIState<T>(isLoading, false, isSuccess, isError)