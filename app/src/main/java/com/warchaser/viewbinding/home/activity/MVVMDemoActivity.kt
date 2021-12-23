package com.warchaser.viewbinding.home.activity

import com.warchaser.libbase.ui.BaseVMActivity
import com.warchaser.libcommonutils.NLog
import com.warchaser.viewbinding.databinding.ActivityMvvmDemoBinding
import com.warchaser.viewbinding.home.viewmodel.MVVMDemoViewModel

class MVVMDemoActivity : BaseVMActivity<ActivityMvvmDemoBinding>(ActivityMvvmDemoBinding::inflate){

    private val mvvmDemoViewModel by lazy {
        getActivityScopeViewModel(MVVMDemoViewModel::class.java)
    }

    override fun setVariable() {
        getViewBound().run {
            viewModel = mvvmDemoViewModel
        }
    }

    override fun startObserve() {
        mvvmDemoViewModel.run {
            vinState.observe(this@MVVMDemoActivity, {
                NLog.d(TAG, it.toString())
            })
        }
    }

}