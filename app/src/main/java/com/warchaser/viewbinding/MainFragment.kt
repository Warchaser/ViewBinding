package com.warchaser.viewbinding

import android.os.Bundle
import android.view.View
import com.warchaser.libbase.ui.BaseFragment
import com.warchaser.viewbinding.databinding.FragmentMainBinding
import com.warchaser.viewbinding.home.contract.MainFragmentView
import com.warchaser.viewbinding.home.presenter.MainFragmentPresenter

class MainFragment : BaseFragment<MainFragmentPresenter, MainFragmentView, FragmentMainBinding>(FragmentMainBinding::inflate){

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        getViewBound().mTv.text = "This part is the MainFragment!!!"
    }

    companion object{
        fun newInstance() : MainFragment{
            return MainFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onLoadPresenter(): MainFragmentPresenter = MainFragmentPresenter(this)
}