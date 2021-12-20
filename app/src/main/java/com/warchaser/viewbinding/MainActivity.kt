package com.warchaser.viewbinding
import android.os.Bundle
import com.warchaser.libbase.ui.BaseActivity
import com.warchaser.viewbinding.databinding.ActivityMainBinding
import com.warchaser.viewbinding.home.activity.MVVMDemoActivity
import com.warchaser.viewbinding.home.activity.ViewBindingDemoActivity
import com.warchaser.viewbinding.home.contract.MainView
import com.warchaser.viewbinding.home.fragment.MainFragment
import com.warchaser.viewbinding.home.presenter.MainPresenter

class MainActivity : BaseActivity<MainPresenter, MainView, ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun afterSetContentView(savedInstanceState: Bundle?) {
        super.afterSetContentView(savedInstanceState)

        getViewBound().mBtnLaunchViewBindingDemo.setOnClickListener {
            startCertainActivity(ViewBindingDemoActivity::class.java)
        }

        getViewBound().mBtnLaunchMVVMDemo.setOnClickListener {
            startCertainActivity(MVVMDemoActivity::class.java)
        }
    }

    override fun onLoadPresenter(): MainPresenter = MainPresenter(this)

}