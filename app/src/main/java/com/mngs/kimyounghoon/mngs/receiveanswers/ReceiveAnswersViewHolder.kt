package com.mngs.kimyounghoon.mngs.receiveanswers

import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.databinding.ItemReceiveAnswerBinding

class ReceiveAnswersViewHolder(val binding: ItemReceiveAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(answer: Answer, userActionListener: LocateListener) {
        binding.answer = answer
        binding.userActionListener = userActionListener
    }
}