package com.warchaser.viewbinding.home.adapter

import com.warchaser.libbase.ui.BaseCommonAdapter
import com.warchaser.viewbinding.databinding.ItemMainBinding

class MainAdapter : BaseCommonAdapter<String, ItemMainBinding>(ItemMainBinding::inflate) {

    override fun onBindViewHolder(vb: ItemMainBinding, position: Int, isFullRefresh: Boolean) {
        super.onBindViewHolder(vb, position, isFullRefresh)
        vb.mTv.text = getItem(position)
    }
}