package com.mngs.kimyounghoon.mngs.letters

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
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
    val empty = ObservableBoolean(false)

    fun start() {
        loadLetters(false)
    }

    fun loadLetters(forceUpdate: Boolean) {
        loadLetters(forceUpdate, true)
    }

    private fun loadLetters(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            isLoading.set(true)
        }
        if (forceUpdate) {

        }

        lettersRepository.loadLetters(object : LettersDataSource.LoadLettersCallback {
            override fun onLettersLoaded(letters: List<Letter>) {
                val lettersToShow: List<Letter> = letters

                if (showLoadingUI) {
                    isLoading.set(false)
                }
                isDataLoadingError.set(false)


                items.set(null)

                items.set(lettersToShow as ArrayList<Letter>?)

                items.get()?.apply {
                    empty.set(isEmpty())
                }

//                        empty.set(isEmpty()))
            }

            override fun onFailedToLoadLetters() {

            }

        })
    }

    fun loadMoreLetters() {
        lettersRepository.loadMoreLetters(object : LettersDataSource.LoadMoreLettersCallback {
            override fun onLettersMoreLoaded(letters: List<Letter>) {
                val lettersToShow: List<Letter> = letters

                isLoading.set(false)
                isDataLoadingError.set(false)

                items.get()?.apply {
                    addAll(size, lettersToShow)
                    empty.set(isEmpty())
                    items.notifyChange()
                }
            }

            override fun onFailedToLoadMoreLetters() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

}