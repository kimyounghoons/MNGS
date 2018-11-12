package com.mngs.kimyounghoon.mngs.reanswers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.BaseRecyclerAdapter
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.FIRST_ITEM
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.SECOND_ITEM
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.databinding.ItemFirstLetterBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemLoadMoreBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemReAnswerBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemSecondAnswerBinding
import com.mngs.kimyounghoon.mngs.letters.LoadMoreViewHolder

class ReAnswersAdapter(private val letter: Letter, private val answer: Answer, var reanswers: List<ReAnswer> = ArrayList(), var isAllLoaded: Boolean = false) : BaseRecyclerAdapter() {


    override fun setItems(prevItemSize: Int, reanswers: List<Any>?) {
        if (reanswers == null || reanswers.isEmpty()) {
            this.reanswers = ArrayList()
            notifyDataSetChanged()
            return
        }

        reanswers.let {
            this.reanswers = reanswers as List<ReAnswer>
            notifyItemRangeInserted(2 + prevItemSize + if (isAllLoaded) {
                0
            } else {
                1
            }, reanswers.size + 2 - prevItemSize)
        }
    }

    override fun setIsAllLoaded(isAllLoaded: Boolean) {
        this.isAllLoaded = isAllLoaded
        notifyDataSetChanged()
        if (isAllLoaded && !reanswers.isEmpty()) {
            notifyItemRemoved(reanswers.size + 2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FIRST_ITEM -> {
                val binding = ItemFirstLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.letter = this.letter
                ReAnswerFirstViewHolder(binding)
            }
            SECOND_ITEM -> {
                val binding = ItemSecondAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.answer = this.answer
                ReAnswerSecondViewHolder(binding)
            }
            Constants.LOAD_MORE
            -> {
                val binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemReAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReAnswerViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2 + reanswers.size + if (isAllLoaded) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReAnswerViewHolder) {
            val reAnswer: ReAnswer? = reanswers[position - 2]
            reAnswer?.apply {
                holder.bind(reAnswer)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> FIRST_ITEM
        position == 1 -> SECOND_ITEM
        position > 2 + reanswers.size - 1 -> Constants.LOAD_MORE
        else -> Constants.ITEM
    }


}