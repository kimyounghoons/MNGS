package com.mngs.kimyounghoon.mngs.myinbox

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.BaseAdapter
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemEmptyMyInboxBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemInboxBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemLoadMoreBinding
import com.mngs.kimyounghoon.mngs.letters.LoadMoreViewHolder

class MyInBoxAdapter(var myLetters: List<Letter> = ArrayList(), var isAllLoaded: Boolean = false) : BaseAdapter() {

    interface ViewType {
        companion object {
            const val EMPTY = 0
            const val ITEM = 1
            const val LOAD_MORE = 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.EMPTY -> {
                val binding = ItemEmptyMyInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyInBoxEmptyViewHolder(binding)
            }
            ViewType.LOAD_MORE
            -> {
                val binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyInBoxViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty() -> ViewType.EMPTY
            position > myLetters.size - 1 -> ViewType.LOAD_MORE
            else -> ViewType.ITEM
        }
    }

    private fun isEmpty(): Boolean {
        return myLetters.isEmpty() && isAllLoaded
    }

    override fun getItemCount(): Int {
        if (isEmpty())
            return 1
        return myLetters.size + if (isAllLoaded) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyInBoxViewHolder) {
            val letter: Letter? = myLetters[position]
            letter?.apply {
                holder.bind(this)
            }
        }
    }

    override fun setItems(prevItemSize: Int, letters: List<Letter>?) {
        if (letters == null || letters.isEmpty()) {
            this.myLetters = ArrayList()
            notifyDataSetChanged()
            return
        }

        letters.let {
            this.myLetters = letters
            notifyItemRangeInserted(prevItemSize + if (isAllLoaded) {
                0
            } else {
                1
            }, letters.size - prevItemSize)
        }
    }

    override fun setIsAllLoaded(isAllLoaded: Boolean) {
        this.isAllLoaded = isAllLoaded
        notifyDataSetChanged()
        if (isAllLoaded && !myLetters.isEmpty()) {
            notifyItemRemoved(myLetters.size)
        }
    }

}