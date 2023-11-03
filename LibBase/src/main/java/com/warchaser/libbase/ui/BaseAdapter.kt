package com.warchaser.libbase.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseBindHolder>() {

    protected val mDataList : ArrayList<T> = ArrayList()

    private var mCurrentIndex : Int = -1

    protected var mItemClickListener : ItemClickListener<T>? = null

    fun getData() : ArrayList<T> = mDataList

    fun isDataEmpty() : Boolean = mDataList.isEmpty()

    protected fun getItem(position: Int) : T = mDataList[position]

    interface ItemClickListener<T>{
        fun onItemClick(position : Int, bean : T, v : View?, holder : BaseBindHolder){

        }

        fun onItemClick(position: Int, bean : T) {

        }
    }

    inner class ClickListenerDelegate(private val holder : BaseBindHolder) : View.OnClickListener{

        override fun onClick(v: View?) {
            val position : Int = holder.bindingAdapterPosition
            if(position == RecyclerView.NO_POSITION){
                return
            }

            v.run {
                val bean : T = getItem(position)
                click(position, bean, v?.id, v, holder)
            }
        }
    }

    fun destroy(){
        mDataList.clear()
        mItemClickListener = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetAllChanged(list : ArrayList<T>){
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun notifyItemsChanged(position: Int){
        notifyItemChanged(mCurrentIndex, "local_refresh")
        notifyItemChanged(position, "local_refresh")
        mCurrentIndex = position
    }

    fun notifyItemsChanged(position : Int, t : T){
        mDataList[position] = t
        notifyItemChanged(position)
    }

    fun notifyAllItemsChangedByRange(){
        for(i in 0 until itemCount){
            notifyItemsChanged(i)
        }
    }

    fun notifyItemsAdd(list : ArrayList<T>){
        val itemStart : Int = itemCount
        mDataList.addAll(list)
        notifyItemRangeInserted(itemStart, list.size)
    }

    protected fun isItemClickListenerNotNull() : Boolean = mItemClickListener != null

    protected open fun click(position: Int, bean : T, id : Int?, v : View?, holder: BaseBindHolder){
        mItemClickListener?.run {
            onItemClick(position, bean, v, holder)
        }
    }

    fun setOnItemClickListener(clickListener : ItemClickListener<T>){
        mItemClickListener = clickListener
    }

    protected fun setClickListener(delegate : ClickListenerDelegate, vararg views : View?){
        for(view in views){
            view?.run {
                setOnClickListener(delegate)
            }
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun getItemId(position: Int): Long = position.toLong()

    protected fun <VB : ViewBinding> castVB(vbClass : Class<VB>, holder: BaseBindHolder) : VB = vbClass.cast(holder.getViewBinding())

    protected fun <L : ItemClickListener<T>> getClickListener(clazz : Class<L>) = clazz.cast(mItemClickListener)
}