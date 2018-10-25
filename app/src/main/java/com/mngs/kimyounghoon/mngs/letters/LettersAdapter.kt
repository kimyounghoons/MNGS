package com.mngs.kimyounghoon.mngs.letters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ItemLetterBinding

class LettersAdapter(var letters: List<Letter> = ArrayList()) : RecyclerView.Adapter<LettersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LettersViewHolder {
        val binding = ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LettersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    override fun onBindViewHolder(holder: LettersViewHolder, position: Int) {
        val letter: Letter? = letters[position]
        letter?.apply {
            holder.bind(this)
        }
    }

    fun setItems(prevItemSize: Int, letters: List<Letter>?) {
        if (letters == null || letters.isEmpty()) {
            this.letters = ArrayList()
            notifyDataSetChanged()
            return
        }

        letters?.let {
            this.letters = letters
            notifyItemRangeInserted(prevItemSize, letters.size - prevItemSize)
        }
    }

}