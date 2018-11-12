package com.mngs.kimyounghoon.mngs.reanswers

import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.databinding.ItemReAnswerBinding

class ReAnswerViewHolder(val itemReanswerBinding: ItemReAnswerBinding): RecyclerView.ViewHolder(itemReanswerBinding.root){

    fun bind(reAnswer: ReAnswer){
        itemReanswerBinding.reanswer = reAnswer
    }
}