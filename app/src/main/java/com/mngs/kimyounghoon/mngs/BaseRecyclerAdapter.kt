package com.mngs.kimyounghoon.mngs

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.data.Letter

abstract class BaseRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    abstract fun setItems(prevItemSize: Int, letters: List<Any>?)

    abstract fun setIsAllLoaded(isAllLoaded: Boolean)
}