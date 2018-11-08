package com.mngs.kimyounghoon.mngs.answers

import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.databinding.ItemAnswersBinding

class AnswersViewHolder(val binding: ItemAnswersBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(answer: Answer, userActionListener: LocateListener) {
        binding.answer = answer
        binding.userActionListener = userActionListener
    }

}