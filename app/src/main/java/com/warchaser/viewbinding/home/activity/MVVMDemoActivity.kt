package com.warchaser.viewbinding.home.activity

import android.os.Bundle
import com.warchaser.libbase.ui.BaseVMActivity
import com.warchaser.viewbinding.databinding.ActivityMvvmDemoBinding

class MVVMDemoActivity : BaseVMActivity<ActivityMvvmDemoBinding>(ActivityMvvmDemoBinding::inflate){

    override fun afterSetContentView(savedInstanceState: Bundle?) {
        super.afterSetContentView(savedInstanceState)
    }

    override fun startObserve() {

    }

}