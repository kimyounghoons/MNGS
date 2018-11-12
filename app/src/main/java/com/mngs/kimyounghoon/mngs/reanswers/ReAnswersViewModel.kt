package com.mngs.kimyounghoon.mngs.reanswers

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class ReAnswersViewModel (private val lettersRepository : LettersRepository) : ViewModel() {
    companion object {
        const val LOAD_MORE_VISIBLE_THRESHOLD = 4
    }

    val isAllLoaded = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isDataLoadingError = ObservableBoolean(false)
    internal val toastMessage = SingleLiveEvent<Int>()
    var items: ObservableField<ArrayList<ReAnswer>> = ObservableField()
    var prevItemSize: ObservableField<Int> = ObservableField(0)
    val empty = ObservableBoolean(false)
    val refreshing = ObservableField<Boolean>(false)
    lateinit var letterId: String

    fun start(letterId : String) {
        this.letterId = letterId
        loadReAnswer(true)
    }

    fun loadReAnswer(forceUpdate: Boolean) {
        loadReAnswer(forceUpdate, true)
    }

    private fun loadReAnswer(forceUpdate: Boolean, showLoadingUI: Boolean) {
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

        lettersRepository.loadReAnswers(letterId,object : LettersDataSource.LoadReAnswersCallback {
            override fun onReAnswersLoaded(reAnswers: List<ReAnswer>) {
                val lettersToShow: List<ReAnswer> = reAnswers

                if (showLoadingUI) {
                    isLoading.set(false)
                }
                isDataLoadingError.set(false)

                items.set(lettersToShow as ArrayList<ReAnswer>?)

                items.get()?.apply {
                    empty.set(isEmpty())
                }

                if (reAnswers.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }
            }

            override fun onFailedToLoadReAnswers() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    fun loadMoreReAnswers() {

        isLoading.set(true)

        lettersRepository.loadMoreReAnswers(object : LettersDataSource.LoadMoreReAnswersCallback{
            override fun onReAnswersMoreLoaded(reAnswers: List<ReAnswer>) {
                val lettersToShow: List<ReAnswer> = reAnswers

                isLoading.set(false)
                isDataLoadingError.set(false)

                prevItemSize.set(items.get()?.size ?: 0)
                items.get()?.apply {
                    addAll(size, lettersToShow)
                    empty.set(isEmpty())
                    items.notifyChange()
                }

                if (reAnswers.size < Constants.LIMIT_PAGE) {
                    isAllLoaded.set(true)
                }
            }

            override fun onFailedToLoadMoreReAnswers() {

            }

        })
    }

}