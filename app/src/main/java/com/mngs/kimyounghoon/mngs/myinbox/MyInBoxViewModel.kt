package com.mngs.kimyounghoon.mngs.myinbox

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class MyInBoxViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {

    override fun loadRepositoryLetter(callback: LettersDataSource.LoadLettersCallback) {
        lettersRepository.loadInBox(callback)
    }

    override fun loadMoreRepositoryLetter(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersRepository.loadMoreInBox(callback)
    }

}