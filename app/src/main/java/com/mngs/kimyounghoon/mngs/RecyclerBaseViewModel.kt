package com.mngs.kimyounghoon.mngs

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource

abstract class RecyclerBaseViewModel : ViewModel() {
    companion object {
        const val LOAD_MORE_VISIBLE_THRESHOLD = 4
    }

    val isAllLoaded = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isDataLoadingError = ObservableBoolean(false)
    internal val toastMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<Letter>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)

    fun start() {
        loadLetters(true)
    }

    fun loadLetters(forceUpdate: Boolean) {
        loadLetters(forceUpdate, true)
    }

    private fun loadLetters(forceUpdate: Boolean, showLoadingUI: Boolean) {
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

        loadRepositoryLetter(object : LettersDataSource.LoadLettersCallback {
            override fun onLettersLoaded(letters: List<Letter>) {
                val lettersToShow: List<Letter> = letters

                if (showLoadingUI) {
                    isLoading.set(false)
                }
                isDataLoadingError.set(false)

                items.set(lettersToShow as ArrayList<Letter>)

                items.get()?.apply {
                    empty.set(isEmpty())
                }

                if (letters.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }

            }

            override fun onFailedToLoadLetters() {

            }

        })
    }

    abstract fun loadRepositoryLetter(callback: LettersDataSource.LoadLettersCallback)

    fun loadMoreLetters() {
        isLoading.set(true)

        loadMoreRepositoryLetter(object : LettersDataSource.LoadMoreLettersCallback {
            override fun onLettersMoreLoaded(letters: List<Letter>) {
                val lettersToShow: List<Letter> = letters

                isLoading.set(false)
                isDataLoadingError.set(false)

                prevItemSize.set(items.get()?.size ?: 0)
                items.get()?.apply {
                    addAll(size, lettersToShow)
                    empty.set(isEmpty())
                    items.notifyChange()
                }

                if (letters.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }
            }

            override fun onFailedToLoadMoreLetters() {
            }
        })
    }

    abstract fun loadMoreRepositoryLetter(callback: LettersDataSource.LoadMoreLettersCallback)

}