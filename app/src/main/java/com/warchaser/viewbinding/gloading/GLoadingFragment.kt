package com.warchaser.viewbinding.gloading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.warchaser.libbase.ui.LazyFragment
import com.warchaser.libbase.ui.gloading.Gloading
import com.warchaser.libbase.ui.gloading.Gloading.TYPE_DEFAULT
import com.warchaser.libbase.ui.presenter.IBasePresenter
import com.warchaser.libbase.ui.presenter.IBaseView

abstract class GLoadingFragment<P : IBasePresenter<V>, V : IBaseView, VB : ViewBinding>(mInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB)
    : LazyFragment<P, V, VB>(mInflate){

    private var mHolder : Gloading.Holder? = null
    private var mCustomRootView : View? = null
    private var mIsLoading : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initLoadingStatusViewIfNecessary()
        return if(isUseCustomRootView()) mRootView else mHolder?.wrapper
    }

    private fun initLoadingStatusViewIfNecessary(){
        if(mHolder == null){
            mHolder = if(isUseCustomRootView()){
                mCustomRootView = mRootView?.findViewById(customRootViewId())
                Gloading.getDefault().wrap(mCustomRootView).withRetry { onRetry() }
            } else {
                Gloading.getDefault().wrap(mRootView).withRetry { onRetry() }
            }
        }
    }

    protected fun onRetry(){

    }

    protected fun showLoading(){
        showLoading(TYPE_DEFAULT)
    }

    protected fun showLoading(type : Int){
        mIsLoading = true
        initLoadingStatusViewIfNecessary()
        mHolder?.showLoading(type)
    }

    protected fun showLoadSuccess(){
        showLoadSuccess(TYPE_DEFAULT)
    }

    protected fun showLoadSuccess(type : Int){
        mIsLoading = false
        initLoadingStatusViewIfNecessary()
        mHolder?.showLoadSuccess(type)
    }

    protected fun showLoadFailed(){
        showLoadFailed(TYPE_DEFAULT)
    }

    protected fun showLoadFailed(type : Int){
        mIsLoading = false
        initLoadingStatusViewIfNecessary()
        mHolder?.showLoadFailed(type)
    }

    protected fun showEmpty(emptyHint: String){
        showEmpty(emptyHint, TYPE_DEFAULT)
    }

    protected fun showEmpty(emptyHint : String, type: Int){
        mIsLoading = false
        initLoadingStatusViewIfNecessary()
        mHolder?.showEmpty(emptyHint, type)
    }

    protected fun isLoading() = mIsLoading

    protected fun isUseCustomRootView() : Boolean = false

    protected fun customRootViewId() : Int = 0
}