package com.mngs.kimyounghoon.mngs.sentanswers

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class SentAnswersViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {
    override fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback) {
        lettersRepository.loadSentAnswers(callback)
    }

    override fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersRepository.loadMoreSentAnswers(callback)
    }

}