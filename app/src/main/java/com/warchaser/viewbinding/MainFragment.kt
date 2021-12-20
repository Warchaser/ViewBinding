package com.warchaser.viewbinding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.warchaser.viewbinding.databinding.FragmentMainBinding
import com.warchaser.viewbinding.gloading.GLoadingFragment
import com.warchaser.viewbinding.home.adapter.MainAdapter
import com.warchaser.viewbinding.home.contract.MainFragmentView
import com.warchaser.viewbinding.home.presenter.MainFragmentPresenter

class MainFragment : GLoadingFragment<MainFragmentPresenter, MainFragmentView, FragmentMainBinding>(FragmentMainBinding::inflate){

    private var mAdapter : MainAdapter? = null

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            showLoadSuccess()
        }, 3000)
        getViewBound().mTv.text = "This part is the MainFragment!!!"
        mAdapter = MainAdapter()
        getViewBound().mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getViewBound().mRecyclerView.adapter = mAdapter
        val list = ArrayList<String>()
        for (i in 0 until 20){
            list.add("$i")
        }
        mAdapter?.notifyDataSetAllChanged(list)
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