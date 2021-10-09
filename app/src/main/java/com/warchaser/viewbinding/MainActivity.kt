package com.warchaser.viewbinding
import android.os.Bundle
import com.warchaser.libbase.ui.BaseActivity
import com.warchaser.viewbinding.databinding.ActivityMainBinding
import com.warchaser.viewbinding.home.contract.MainView
import com.warchaser.viewbinding.home.presenter.MainPresenter

class MainActivity : BaseActivity<MainPresenter, MainView, ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun afterSetContentView(savedInstanceState: Bundle?) {
        super.afterSetContentView(savedInstanceState)
        mViewBond.mTv.text = "123"
    }

    override fun onLoadPresenter(): MainPresenter = MainPresenter(this)

}