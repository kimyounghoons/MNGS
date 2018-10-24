package com.mngs.kimyounghoon.mngs.letters

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.letters.LettersViewModel.Companion.LOAD_MORE_VISIBLE_THRESHOLD

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: ObservableField<ArrayList<Letter>>) {
    with(recyclerView.adapter as LettersAdapter) {
        setItems(items.get())
    }
}

@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.OnRefreshListener(viewModel: LettersViewModel) {
    setOnRefreshListener {
        viewModel.loadLetters(true)
    }
}

@BindingAdapter("app:refreshing")
fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, isLoading: Boolean) {
    swipeRefreshLayout.isRefreshing = isLoading
}

@BindingAdapter("android:onScroll")
fun RecyclerView.addOnScrollListener(viewModel: LettersViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + LOAD_MORE_VISIBLE_THRESHOLD) {
                if (adapter != null)
                    viewModel.loadMoreLetters()
            }
        }
    })
}