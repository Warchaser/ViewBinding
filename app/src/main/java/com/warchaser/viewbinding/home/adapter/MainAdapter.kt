package com.warchaser.viewbinding.home.adapter

import android.view.View
import com.warchaser.libbase.ui.BaseCommonAdapter
import com.warchaser.viewbinding.R
import com.warchaser.viewbinding.databinding.ItemMainBinding

class MainAdapter : BaseCommonAdapter<String, ItemMainBinding>(ItemMainBinding::inflate) {

    override fun onBindViewHolder(vb: ItemMainBinding, position: Int, isFullRefresh: Boolean) {
        super.onBindViewHolder(vb, position, isFullRefresh)
        vb.mTv.text = getItem(position)
    }

    override fun onCreateViewHolder(rootView: View, viewBinding: ItemMainBinding, viewHolder: BaseBindHolder) {
        super.onCreateViewHolder(rootView, viewBinding, viewHolder)
        setClickListener(ClickListenerDelegate(viewHolder), rootView, viewBinding.mTv)
    }

    override fun click(position: Int, bean: String, id: Int) {
        super.click(position, bean, id)
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
}