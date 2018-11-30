package com.mngs.kimyounghoon.mngs.reanswers

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class ReAnswersViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {

    lateinit var letterId: String
    internal val hasLetter = SingleLiveEvent<Letter>()
    var itemPosition = ObservableInt()
    var needNext = ObservableBoolean()

    override fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback) {
        lettersRepository.loadReAnswers(letterId, callback)
    }

    override fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersRepository.loadMoreReAnswers(callback)
    }

    fun start(letterId: String) {
        this.letterId = letterId
        loadItems(true)
    }

    fun getLetter(letterId: String) {
        lettersRepository.getLetter(letterId, object : LettersDataSource.GetLetterCallback {
            override fun onLetterLoaded(letter: Letter) {
                hasLetter.value = letter
            }

            override fun onFailedToLoadLetters() {
            }
        })
    }

}