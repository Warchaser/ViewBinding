package com.warchaser.libbase.ui.presenter

import android.content.Context
import com.trello.rxlifecycle3.LifecycleProvider

/**
 * 需要传入LifecycleProvider
 * @param mLifecycleProvider RxLifeCycle的Provider
 */
open class BasePresenter<V : IBaseView?>(protected var mLifecycleProvider: LifecycleProvider<*>) : IBasePresenter<V> {
    protected var view: V? = null
        private set

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun setContext(context: Context?) {}
}