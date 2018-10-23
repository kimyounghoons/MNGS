package com.mngs.kimyounghoon.mngs.letters

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.Letter

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: List<Letter>) {
    with(recyclerView.adapter as LettersAdapter){
        setItems(items)
    }
}

