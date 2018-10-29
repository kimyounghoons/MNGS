package com.mngs.kimyounghoon.mngs.myinbox

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class MyInBoxViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {

    companion object {
        const val LOAD_MORE_VISIBLE_THRESHOLD = 4
    }

    val isDataLoadingError = ObservableBoolean(false)
    val loadLettersCommand = SingleLiveEvent<Void>()
    internal val snackbarMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<Letter>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)

    override fun loadLetters(forceUpdate: Boolean, showLoadingUI: Boolean) {
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

        lettersRepository.loadInBox(object : LettersDataSource.LoadInBoxCallback {
            override fun onInBoxLoaded(letters: List<Letter>) {
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

            override fun onFailedToLoadInBox() {
            }

        })
    }

    override fun loadMoreLetters() {
        isLoading.set(true)

        lettersRepository.loadMoreInBox(object : LettersDataSource.LoadMoreInBoxCallback {
            override fun onInBoxMoreLoaded(letters: List<Letter>) {
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

            override fun onFailedToLoadMoreInBox() {

            }
        })
    }

}