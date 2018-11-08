package com.mngs.kimyounghoon.mngs.letters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.BaseRecyclerAdapter
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY_ITEM
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ITEM

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LOAD_MORE
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemEmptyBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemLetterBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemLoadMoreBinding

class LettersAdapter(private val userActionListener: LocateListener, var letters: List<Letter> = ArrayList(), var isAllLoaded: Boolean = false) : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_ITEM -> {
                val binding = ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LettersEmptyViewHolder(binding)
            }
            LOAD_MORE
            -> {
                val binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LettersViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty() -> EMPTY_ITEM
            position > letters.size - 1 -> LOAD_MORE
            else -> ITEM
        }
    }

    private fun isEmpty(): Boolean {
        return letters.isEmpty() && isAllLoaded
    }

    override fun getItemCount(): Int {
        if (isEmpty())
            return 1
        return letters.size + if (isAllLoaded) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LettersViewHolder) {
            val letter: Letter? = letters[position]
            letter?.apply {
                holder.bind(this, userActionListener)
            }
        }
    }

    override fun setItems(prevItemSize: Int, letters: List<Any>?) {
        if (letters == null || letters.isEmpty()) {
            this.letters = ArrayList()
            notifyDataSetChanged()
            return
        }

        letters.let {
            this.letters = letters as List<Letter>
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
        if (isAllLoaded && !letters.isEmpty()) {
            notifyItemRemoved(letters.size)
        }
    }

}