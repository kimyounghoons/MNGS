package com.mngs.kimyounghoon.mngs.letters

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class LettersViewModel(context: Application, private val lettersRepository: LettersRepository) : AndroidViewModel(context) {

    companion object {
        const val LOAD_MORE_VISIBLE_THRESHOLD = 4
    }

    val isDataLoadingError = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val loadLettersCommand = SingleLiveEvent<Void>()
    internal val snackbarMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<Letter>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val isAllLoaded = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)

    fun start() {
        loadLetters(false)
    }

    fun loadLetters(forceUpdate: Boolean) {
        loadLetters(forceUpdate, true)
    }

    private fun loadLetters(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            if(refreshing.get()==false){
                refreshing.notifyChange()
            }else {
                refreshing.set(false)
            }
            isLoading.set(true)
        }
        if (forceUpdate) {
            items.set(ArrayList())
            prevItemSize.set(0)
            isAllLoaded.set(false)
        }

        lettersRepository.loadLetters(object : LettersDataSource.LoadLettersCallback {
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

            }

            override fun onFailedToLoadLetters() {

            }

        })
    }

    fun loadMoreLetters() {
        isLoading.set(true)

        lettersRepository.loadMoreLetters(object : LettersDataSource.LoadMoreLettersCallback {
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

}