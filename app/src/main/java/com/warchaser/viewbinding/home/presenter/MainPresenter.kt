package com.warchaser.viewbinding.home.presenter

import com.trello.rxlifecycle3.LifecycleProvider
import com.warchaser.libbase.ui.presenter.BasePresenter
import com.warchaser.viewbinding.home.contract.MainView

class MainPresenter(mLifecycleProvider: LifecycleProvider<*>) :
    BasePresenter<MainView>(mLifecycleProvider) {
}