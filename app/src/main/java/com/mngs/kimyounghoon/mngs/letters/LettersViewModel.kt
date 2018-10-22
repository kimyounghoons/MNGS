package com.mngs.kimyounghoon.mngs.letters

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.util.Log
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class LettersViewModel(context: Application, private val lettersRepository: LettersRepository) : AndroidViewModel(context) {

    private val isDataLoadingError = ObservableBoolean(false)
    private val dataLoading = ObservableBoolean(false)
    private val loadLettersCommand = SingleLiveEvent<Void>()
    internal val snackbarMessage = SingleLiveEvent<Int>()
    internal var items: ObservableList<Letter> = ObservableArrayList()
    val empty = ObservableBoolean(false)

    fun loadLetters(forceUpdate: Boolean) {
        loadLetters(forceUpdate, true)
    }

    private fun loadLetters(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            dataLoading.set(true)
        }
        if(forceUpdate){

        }

        lettersRepository.loadLetters(object : LettersDataSource.LoadLettersCallback {
            override fun onLettersLoaded(letters: List<Letter>) {
                val lettersToShow: List<Letter>

                if (showLoadingUI) {
                    dataLoading.set(false)
                }
                isDataLoadingError.set(false)

                lettersToShow = letters

                with(items) {
                    clear()
                    addAll(lettersToShow)
                    empty.set(isEmpty())
                }
            }

            override fun onFailedToLoadLetters() {

            }

        })
    }

}