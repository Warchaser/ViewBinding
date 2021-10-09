package com.warchaser.libbase.ui.presenter

import android.content.Context

interface IBasePresenter<V : IBaseView?> {
    fun attachView(view : V)
    fun detachView()
    fun setContext(context: Context?)
}