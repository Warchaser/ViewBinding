package com.warchaser.libbase.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BaseCommonAdapter<T, VB : ViewBinding>(mInflate : (LayoutInflater, ViewGroup, Boolean) -> VB) : BaseBindAdapter<T, VB, BaseBindAdapter.BaseBindHolder>(mInflate)