package com.mngs.kimyounghoon.mngs.letters

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class LettersViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {
    override fun loadRepositoryLetter(callback: LettersDataSource.LoadLettersCallback) {
        lettersRepository.loadLetters(callback)
    }

    override fun loadMoreRepositoryLetter(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersRepository.loadMoreLetters(callback)
    }

}