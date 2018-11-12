package com.mngs.kimyounghoon.mngs

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel.Companion.LOAD_MORE_VISIBLE_THRESHOLD
import com.mngs.kimyounghoon.mngs.answers.AnswersViewModel
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY_ITEM
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LOAD_MORE
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.reanswer.ReAnswerViewModel
import com.mngs.kimyounghoon.mngs.reanswers.ReAnswersViewModel

@BindingAdapter("bind:items", "bind:prev")
fun setItems(recyclerView: RecyclerView, items: List<Letter>?, prevItemSize: Int) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setItems(prevItemSize, items)
    }
}

@BindingAdapter("bind:isAllLoaded")
fun setIsAllLoaded(recyclerView: RecyclerView, isAlloaded: Boolean?) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setIsAllLoaded(isAlloaded ?: false)
    }
}

@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.OnRefreshListener(viewModel: RecyclerBaseViewModel) {
    setOnRefreshListener {
        viewModel.loadLetters(true)
    }
}

@BindingAdapter("app:refreshing")
fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean?) {
    swipeRefreshLayout.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("android:onScroll")
fun RecyclerView.addOnScrollListener(viewModel: RecyclerBaseViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + LOAD_MORE_VISIBLE_THRESHOLD) {
                if (adapter != null && !viewModel.isAllLoaded.get() && !viewModel.isLoading.get())
                    viewModel.loadMoreLetters()
            }
        }
    })
}


@BindingAdapter("bind:layoutManagerWithDecoration")
fun RecyclerView.setLayoutManagerWithDecoration(gridSize: Int) {
    val gridLayoutManager = GridLayoutManager(context, gridSize)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (adapter.getItemViewType(position)) {
                EMPTY_ITEM -> gridSize
                LOAD_MORE -> gridSize
                else -> 1
            }
        }
    }
    layoutManager = gridLayoutManager
    addItemDecoration(GridItemDecoration(1, gridSize))
}





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



@BindingAdapter("bind:answers", "bind:prevAnswers")
fun setAnswers(recyclerView: RecyclerView, items: List<Answer>?, prevItemSize: Int) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setItems(prevItemSize, items)
    }
}

@BindingAdapter("bind:isAllLoadedAnswers")
fun setIsAllLoadedAnswers(recyclerView: RecyclerView, isAlloaded: Boolean?) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setIsAllLoaded(isAlloaded ?: false)
    }
}

@BindingAdapter("android:onRefreshAnswers")
fun SwipeRefreshLayout.OnRefreshListener(viewModel: AnswersViewModel) {
    setOnRefreshListener {
        viewModel.loadAnswers(true)
    }
}

@BindingAdapter("app:refreshingAnswers")
fun setSwipeRefreshLayoutAnswers(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean?) {
    swipeRefreshLayout.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("android:onScrollAnswers")
fun RecyclerView.addOnScrollListenerAnswers(viewModel: AnswersViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + LOAD_MORE_VISIBLE_THRESHOLD) {
                if (adapter != null && !viewModel.isAllLoaded.get() && !viewModel.isLoading.get())
                    viewModel.loadMoreAnswers()
            }
        }
    })
}

////////////////////////////////////////////////////////////////////////////////////////////


@BindingAdapter("bind:reAnswers", "bind:prevReAnswers")
fun setReAnswers(recyclerView: RecyclerView, items: List<ReAnswer>?, prevItemSize: Int) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setItems(prevItemSize, items)
    }
}

@BindingAdapter("bind:isAllLoadedReAnswers")
fun setIsAllLoadedReAnswers(recyclerView: RecyclerView, isAlloaded: Boolean?) {
    with(recyclerView.adapter as BaseRecyclerAdapter) {
        setIsAllLoaded(isAlloaded ?: false)
    }
}

@BindingAdapter("android:onRefreshReAnswers")
fun SwipeRefreshLayout.OnRefreshListener(viewModel: ReAnswersViewModel) {
    setOnRefreshListener {
        viewModel.loadReAnswer(true)
    }
}

@BindingAdapter("app:refreshingReAnswers")
fun setSwipeRefreshLayoutReAnswers(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean?) {
    swipeRefreshLayout.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("android:onScrollReAnswers")
fun RecyclerView.addOnScrollListenerReAnswers(viewModel: ReAnswersViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + LOAD_MORE_VISIBLE_THRESHOLD) {
                if (adapter != null && !viewModel.isAllLoaded.get() && !viewModel.isLoading.get())
                    viewModel.loadMoreReAnswers()
            }
        }
    })
}