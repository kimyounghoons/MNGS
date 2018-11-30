package com.mngs.kimyounghoon.mngs

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.data.AbstractId
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource

abstract class RecyclerBaseViewModel : ViewModel() {
    companion object {
        const val LOAD_MORE_VISIBLE_THRESHOLD = 4
    }

    val isAllLoaded = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isDataLoadingError = ObservableBoolean(false)
    internal val toastMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<AbstractId>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)

    fun start() {
        loadItems(true)
    }

    fun loadItems(forceUpdate: Boolean) {
        loadItems(forceUpdate, true)
    }

    private fun loadItems(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            if (refreshing.get() == false) {
                refreshing.notifyChange()
            } else {
                refreshing.set(false)
            }
            isLoading.set(true)
        }
        if (forceUpdate) {
            items.set(ArrayList())
            prevItemSize.set(0)
            isAllLoaded.set(false)
        }

        loadRepositoryItems(object : LettersDataSource.LoadItemsCallback {
            override fun onLoaded(items: List<AbstractId>) {
                val itemsToShow: List<AbstractId> = items

                if (showLoadingUI) {
                    isLoading.set(false)
                }
                isDataLoadingError.set(false)

                this@RecyclerBaseViewModel.items.set(itemsToShow as ArrayList<AbstractId>)

                this@RecyclerBaseViewModel.items.get()?.apply {
                    empty.set(isEmpty())
                }

                if (items.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }

            }

            override fun onFailedToLoadItems() {

            }

        })
    }

    abstract fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback)

    fun loadMoreItems() {
        isLoading.set(true)

        loadMoreRepositoryItems(object : LettersDataSource.LoadMoreItemsCallback {
            override fun onMoreLoaded(items: List<AbstractId>) {
                val itemsToShow: List<AbstractId> = items

                isLoading.set(false)
                isDataLoadingError.set(false)

                prevItemSize.set(this@RecyclerBaseViewModel.items.get()?.size ?: 0)
                this@RecyclerBaseViewModel.items.get()?.apply {
                    addAll(size, itemsToShow)
                    empty.set(isEmpty())
                    this@RecyclerBaseViewModel.items.notifyChange()
                }

                if (items.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }
            }

            override fun onFailedToLoadMoreItems() {

            }
        })
    }

    abstract fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback)

}