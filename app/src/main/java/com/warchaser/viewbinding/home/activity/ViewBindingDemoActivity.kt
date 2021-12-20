package com.warchaser.viewbinding.home.activity

import android.os.Bundle
import com.warchaser.libbase.ui.BaseActivity
import com.warchaser.viewbinding.R
import com.warchaser.viewbinding.databinding.ActivityViewBindingDemoBinding
import com.warchaser.viewbinding.home.fragment.MainFragment

class ViewBindingDemoActivity :
    BaseActivity<Nothing, Nothing, ActivityViewBindingDemoBinding>(ActivityViewBindingDemoBinding::inflate) {

    override fun afterSetContentView(savedInstanceState: Bundle?) {
        super.afterSetContentView(savedInstanceState)

        getViewBound().mTv.text = "This part is the ViewBindingDemoActivity!!!"

        initializeFragment()
    }

    private fun initializeFragment() {
        loadRootFragment(R.id.mContainer, MainFragment.newInstance())
    }

    override fun onLoadPresenter(): Nothing? = null
}