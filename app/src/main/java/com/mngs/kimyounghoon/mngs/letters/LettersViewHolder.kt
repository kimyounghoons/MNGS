package com.mngs.kimyounghoon.mngs.letters

import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemLetterBinding

class LettersViewHolder(val binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(letter: Letter, userActionListener: LocateListener) {
        binding.letter = letter
        binding.userActionListener = userActionListener
    }

}