package com.warchaser.viewbinding.home.adapter

import android.view.View
import androidx.viewbinding.ViewBinding
import com.warchaser.libbase.ui.BaseCommonAdapter
import com.warchaser.viewbinding.databinding.ItemMainBinding

class MainAdapter() : BaseCommonAdapter<String, ItemMainBinding>(ItemMainBinding::inflate) {

    override fun onBindViewHolder(vb: ItemMainBinding, position: Int, isFullRefresh: Boolean) {
        super.onBindViewHolder(vb, position, isFullRefresh)
        vb.mTv.text = getItem(position)
    }

    override fun onCreateViewHolder(rootView: View, VB: ViewBinding, viewHolder: BaseBindHolder) {
        super.onCreateViewHolder(rootView, VB, viewHolder)
        val clickListenerDelegate = ClickListenerDelegate(viewHolder)
        rootView.setOnClickListener(clickListenerDelegate)
    }

    override fun click(position: Int, bean: String, id: Int) {
        super.click(position, bean, id)
        mItemClickListener?.onItemClick(position, bean)
    }
}