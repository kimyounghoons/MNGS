package com.mngs.kimyounghoon.mngs.receiveanswers

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class ReceiveAnswersViewModel (private val lettersRepository: LettersRepository) : ViewModel() {

    val isAllLoaded = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isDataLoadingError = ObservableBoolean(false)
    internal val toastMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<Answer>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)

    fun start() {
        loadReceiveAnswers(true)
    }

    fun loadReceiveAnswers(forceUpdate: Boolean) {
        loadReceiveAnswers(forceUpdate, true)
    }

    private fun loadReceiveAnswers(forceUpdate: Boolean, showLoadingUI: Boolean) {
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

        lettersRepository.loadReceiveAnswers(object : LettersDataSource.LoadAnswersCallback {

            override fun onAnswersLoaded(answers: List<Answer>) {
                val lettersToShow: List<Answer> = answers

                if (showLoadingUI) {
                    isLoading.set(false)
                }
                isDataLoadingError.set(false)

                items.set(lettersToShow as ArrayList<Answer>?)

                items.get()?.apply {
                    empty.set(isEmpty())
                }

                if (answers.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }

            }

            override fun onFailedToLoadAnswers() {

            }


        })
    }

    fun loadMoreReceiveAnswers() {

        isLoading.set(true)

        lettersRepository.loadMoreReceiveAnswers(object : LettersDataSource.LoadMoreAnswersCallback{
            override fun onAnswersMoreLoaded(answers: List<Answer>) {
                val lettersToShow: List<Answer> = answers

                isLoading.set(false)
                isDataLoadingError.set(false)

                prevItemSize.set(items.get()?.size ?: 0)
                items.get()?.apply {
                    addAll(size, lettersToShow)
                    empty.set(isEmpty())
                    items.notifyChange()
                }

                if (answers.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }

            }

            override fun onFailedToLoadMoreAnswers() {
            }

        })
    }

}