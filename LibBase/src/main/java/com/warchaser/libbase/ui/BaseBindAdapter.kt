package com.warchaser.libbase.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.viewbinding.ViewBinding

abstract class BaseBindAdapter<T, VB : ViewBinding, VH : BaseBindHolder> : BaseAdapter<T>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : VH {
        val viewBinding : VB = inflate().invoke(LayoutInflater.from(parent.context), parent, false)
        val viewHolder : BaseBindHolder = BaseBindHolder(viewBinding)
        val baseViewHolder : VH = viewHolder as VH
        onCreateViewHolder(viewBinding.root, viewBinding, baseViewHolder, viewType)
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        onBindViewHolder(holder, position, ArrayList())
    }

    override fun onBindViewHolder(
        holder: BaseBindHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, holder.getViewBinding() as VB, position, payloads.isEmpty())
    }

    protected open fun onBindViewHolder(@NonNull holder : BaseBindHolder, vb : VB, position: Int, isFullRefresh : Boolean){

    }

    /**
     * 请重写此函数,以便实现ClickListener绑定等逻辑
     */
    protected open fun onCreateViewHolder(rootView : View, viewBinding : VB, viewHolder : VH, viewType : Int){

    }

    protected abstract fun inflate() : Function3<LayoutInflater, ViewGroup, Boolean, VB>

}