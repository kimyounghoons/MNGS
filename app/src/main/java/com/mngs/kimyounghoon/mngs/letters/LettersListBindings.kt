package com.mngs.kimyounghoon.mngs.letters

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.Letter

object LettersListBindings {
    @BindingAdapter("bind:item")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, letters: ObservableArrayList<Letter>) {
        recyclerView.adapter
    }
}
