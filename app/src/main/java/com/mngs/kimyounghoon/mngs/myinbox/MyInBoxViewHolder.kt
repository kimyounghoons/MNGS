package com.mngs.kimyounghoon.mngs.myinbox

import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemInboxBinding

class MyInBoxViewHolder(val binding: ItemInboxBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(letter: Letter) {
        binding.letter = letter
    }

}