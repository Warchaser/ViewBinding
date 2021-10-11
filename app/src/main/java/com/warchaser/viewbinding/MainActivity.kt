package com.warchaser.viewbinding
import android.os.Bundle
import com.warchaser.libbase.ui.BaseActivity
import com.warchaser.viewbinding.databinding.ActivityMainBinding
import com.warchaser.viewbinding.home.contract.MainView
import com.warchaser.viewbinding.home.presenter.MainPresenter

class MainActivity : BaseActivity<MainPresenter, MainView, ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun afterSetContentView(savedInstanceState: Bundle?) {
        super.afterSetContentView(savedInstanceState)
        initializeFragment()
        getViewBound().mTv.text = "This part is the MainActivity!!!"
    }

    private fun initializeFragment(){
        loadRootFragment(R.id.mContainer, MainFragment.newInstance())
    }

    override fun onLoadPresenter(): MainPresenter = MainPresenter(this)

}