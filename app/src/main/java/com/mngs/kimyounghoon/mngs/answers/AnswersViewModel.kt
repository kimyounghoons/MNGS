package com.mngs.kimyounghoon.mngs.answers

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class AnswersViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {

    lateinit var letterId: String

    override fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback) {
        lettersRepository.loadAnswers(letterId, callback)
    }

    override fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersRepository.loadMoreLetters(callback)
    }

    fun start(letterId: String) {
        this.letterId = letterId
        loadItems(true)
    }

}