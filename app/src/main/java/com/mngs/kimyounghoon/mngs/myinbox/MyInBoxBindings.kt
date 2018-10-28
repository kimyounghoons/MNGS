package com.mngs.kimyounghoon.mngs.myinbox

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.letters.LettersViewModel

@BindingAdapter("bind:inbox_items", "bind:inbox_prev")
fun setItems(recyclerView: RecyclerView, items: List<Letter>?, prevItemSize: Int) {
    with(recyclerView.adapter as MyInBoxAdapter) {
        setItems(prevItemSize, items)
    }
}

@BindingAdapter("bind:inbox_isAllLoaded")
fun setIsAllLoaded(recyclerView: RecyclerView, isAlloaded: Boolean?) {
    with(recyclerView.adapter as MyInBoxAdapter) {
        setIsAllLoaded(isAlloaded ?: false)
    }
}

@BindingAdapter("bind:inbox_onRefresh")
fun SwipeRefreshLayout.OnRefreshListener(viewModel: MyInBoxViewModel) {
    setOnRefreshListener {
        viewModel.loadInBox(true)
    }
}

@BindingAdapter("bind:inbox_refreshing")
fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean?) {
    swipeRefreshLayout.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("bind:inbox_onScroll")
fun RecyclerView.addOnScrollListener(viewModel: MyInBoxViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + LettersViewModel.LOAD_MORE_VISIBLE_THRESHOLD) {
                if (adapter != null && !viewModel.isAllLoaded.get() && !viewModel.isLoading.get())
                    viewModel.loadMoreInBox()
            }
        }
    })
}