package com.warchaser.libbase.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseBindAdapter<T, VB : ViewBinding, VH : BaseBindAdapter.BaseBindHolder>(private val mInflate : (LayoutInflater, ViewGroup, Boolean) -> VB)
    : RecyclerView.Adapter<VH>(){

    protected val mDataList : ArrayList<T> = ArrayList()

    private var mCurrentIndex = -1

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetAllChanged(dataList : ArrayList<T>){
        mDataList.clear()
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun notifyItemsChanged(position: Int){
        notifyItemChanged(mCurrentIndex, "local_refresh")
        notifyItemChanged(position, "local_refresh")
        mCurrentIndex = position
    }

    fun notifyAllItemsChangedByRange(){
        for(i in 0 until itemCount){
            notifyItemsChanged(i)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val baseViewHolder : VH
        val viewBinding = mInflate(LayoutInflater.from(parent.context), parent, false)
        onCreateViewHolder(viewBinding.root, viewBinding)
        val viewHolder = BaseBindHolder(viewBinding)
        baseViewHolder = viewHolder as VH
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position, ArrayList())
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        holder.getViewBinging<VB>().apply {
            onBindViewHolder(this, position, payloads.isEmpty())
        }
    }

    protected open fun onBindViewHolder(vb : VB, position: Int, isFullRefresh : Boolean){

    }

    /**
     * 请重写此函数,以便实现ClickListener绑定等逻辑
     * */
    protected open fun onCreateViewHolder(rootView : View, VB : ViewBinding){

    }

    override fun getItemCount(): Int = mDataList.size

    protected fun getItem(position: Int) : T = mDataList[position]

    open class BaseBindHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
        constructor(itemView : View) : this(ViewBinding { itemView })

        fun <VB : ViewBinding> getViewBinging() = binding as VB
    }

}