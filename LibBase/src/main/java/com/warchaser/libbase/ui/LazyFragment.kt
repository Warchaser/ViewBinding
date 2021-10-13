package com.warchaser.libbase.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.warchaser.libbase.ui.presenter.IBasePresenter
import com.warchaser.libbase.ui.presenter.IBaseView

abstract class LazyFragment <P : IBasePresenter<V>, V : IBaseView, VB : ViewBinding>(mInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB)
    : BaseFragment<P, V, VB>(mInflate){
    private var mIsLoaded = false
    private var mIsVisibleToUser = false

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mIsVisibleToUser = hidden.not()
        judgeLazyInit()
    }

    protected open fun onVisibleOnce(){

    }

    private fun judgeLazyInit(){
        if(mIsLoaded.not() && isHidden.not()){
            onVisibleOnce()
            mIsLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsLoaded = false
    }
}