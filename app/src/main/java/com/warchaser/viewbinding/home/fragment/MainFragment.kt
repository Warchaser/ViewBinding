package com.warchaser.viewbinding.home.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.warchaser.libbase.ui.BaseBindAdapter
import com.warchaser.viewbinding.databinding.FragmentMainBinding
import com.warchaser.viewbinding.gloading.GLoadingFragment
import com.warchaser.viewbinding.home.adapter.MainAdapter
import com.warchaser.viewbinding.home.contract.MainFragmentView
import com.warchaser.viewbinding.home.presenter.MainFragmentPresenter

class MainFragment : GLoadingFragment<MainFragmentPresenter, MainFragmentView, FragmentMainBinding>(FragmentMainBinding::inflate){

    private var mAdapter : MainAdapter? = null

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        showLoading()
        getViewBound().mTv.text = "This part is the MainFragment!!!"
        mAdapter = MainAdapter()
        mAdapter?.setOnItemClickListener(object : BaseBindAdapter.ItemClickListener<String>{
            override fun onItemClick(position: Int, bean: String) {
                Toast.makeText(requireContext(), bean, Toast.LENGTH_SHORT).show()
            }
        })
        getViewBound().mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getViewBound().mRecyclerView.adapter = mAdapter
        val list = ArrayList<String>()
        for (i in 0 until 20){
            list.add("$i")
        }
        mAdapter?.notifyDataSetAllChanged(list)
        Handler(Looper.getMainLooper()).postDelayed({
            showLoadSuccess()
        }, 3000)
    }

    companion object{
        fun newInstance() : MainFragment {
            return MainFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onLoadPresenter(): MainFragmentPresenter = MainFragmentPresenter(this)
}