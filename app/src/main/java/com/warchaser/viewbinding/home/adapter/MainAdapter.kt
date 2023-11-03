package com.warchaser.viewbinding.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.warchaser.libbase.ui.BaseBindHolder
import com.warchaser.libbase.ui.BaseCommonAdapter
import com.warchaser.viewbinding.R
import com.warchaser.viewbinding.databinding.ItemMainBinding

class MainAdapter : BaseCommonAdapter<String, ItemMainBinding>(ItemMainBinding::inflate) {

    override fun onBindViewHolder(
        holder: BaseBindHolder,
        vb: ItemMainBinding,
        position: Int,
        isFullRefresh: Boolean
    ) {
        super.onBindViewHolder(holder, vb, position, isFullRefresh)
        vb.mTv.text = " $position"
    }

    override fun onCreateViewHolder(
        rootView: View,
        viewBinding: ItemMainBinding,
        viewHolder: BaseBindHolder,
        viewType: Int
    ) {
        super.onCreateViewHolder(rootView, viewBinding, viewHolder, viewType)
        setClickListener(ClickListenerDelegate(viewHolder), rootView, viewBinding.mTv)
    }

    override fun click(position: Int, bean: String, id: Int?, v: View?, holder: BaseBindHolder) {
        super.click(position, bean, id, v, holder)
        mItemClickListener?.run {
            (this as MainClickListener).run {
                when(id){
                    R.id.mLyRoot -> onItemClick(position, bean)
                    R.id.mTv -> onTextClick(bean)
                }
            }
        }
    }

    interface MainClickListener : ItemClickListener<String>{
        fun onTextClick(bean: String)
    }

    override fun inflate(): (LayoutInflater, ViewGroup, Boolean) -> ItemMainBinding {
        return ItemMainBinding::inflate
    }
}