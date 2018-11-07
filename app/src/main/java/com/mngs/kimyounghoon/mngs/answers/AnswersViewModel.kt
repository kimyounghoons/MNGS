package com.mngs.kimyounghoon.mngs.answers

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class AnswersViewModel (private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {
    override fun loadRepositoryLetter(callback: LettersDataSource.LoadLettersCallback) {
        lettersRepository.loadAnswers(callback)
    }

    override fun loadMoreRepositoryLetter(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersRepository.loadMoreAnswers(callback)
    }

}