package com.warchaser.libbase.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseBindHolder(private val binding : ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun <VBT : ViewBinding> getViewBinding() : VBT = binding as VBT

    fun getContext() : Context = itemView.context

    fun getString(resId : Int) : String = getContext().getString(resId)

    fun getPixelSize(resId: Int) : Int = getContext().resources.getDimensionPixelSize(resId)

}