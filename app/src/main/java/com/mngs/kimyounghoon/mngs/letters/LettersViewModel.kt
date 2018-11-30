package com.mngs.kimyounghoon.mngs.letters

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class LettersViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {
    override fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback) {
        lettersRepository.loadLetters(callback)
    }

    override fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersRepository.loadMoreLetters(callback)
    }

}