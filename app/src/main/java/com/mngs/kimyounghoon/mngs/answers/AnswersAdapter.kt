package com.mngs.kimyounghoon.mngs.answers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.BaseRecyclerAdapter
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemAnswersBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemEmptyAnswersBinding
import com.mngs.kimyounghoon.mngs.databinding.ItemLoadMoreBinding
import com.mngs.kimyounghoon.mngs.letters.LoadMoreViewHolder

class AnswersAdapter(private val letter : Letter, private val userActionListener: LocateListener, var answers: List<Answer> = ArrayList(), var isAllLoaded: Boolean = false) : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.EMPTY_ITEM -> {
                val binding = ItemEmptyAnswersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AnswersEmptyViewHolder(binding)
            }
            Constants.LOAD_MORE
            -> {
                val binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemAnswersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AnswersViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty() -> Constants.EMPTY_ITEM
            position > answers.size - 1 -> Constants.LOAD_MORE
            else -> Constants.ITEM
        }
    }

    private fun isEmpty(): Boolean {
        return answers.isEmpty() && isAllLoaded
    }

    override fun getItemCount(): Int {
        if (isEmpty())
            return 1
        return answers.size + if (isAllLoaded) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnswersViewHolder) {
            val answer: Answer? = answers[position]
            answer?.apply {
                holder.bind(letter,this, userActionListener)
            }
        }
    }

    override fun setItems(prevItemSize: Int, answers: List<Any>?) {

        if (answers == null || answers.isEmpty()) {
            this.answers = ArrayList()
            notifyDataSetChanged()
            return
        }

        answers.let {
            this.answers = answers as List<Answer>
            notifyItemRangeInserted(prevItemSize + if (isAllLoaded) {
                0
            } else {
                1
            }, answers.size - prevItemSize)
        }
    }

    override fun setIsAllLoaded(isAllLoaded: Boolean) {
        this.isAllLoaded = isAllLoaded
        notifyDataSetChanged()
        if (isAllLoaded && !answers.isEmpty()) {
            notifyItemRemoved(answers.size)
        }
    }

}