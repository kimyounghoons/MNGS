package com.mngs.kimyounghoon.mngs.myinbox

import com.mngs.kimyounghoon.mngs.RecyclerBaseViewModel
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class MyInBoxViewModel(private val lettersRepository: LettersRepository) : RecyclerBaseViewModel() {

    override fun loadRepositoryItems(callback: LettersDataSource.LoadItemsCallback) {
        lettersRepository.loadInBox(callback)
    }

    override fun loadMoreRepositoryItems(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersRepository.loadMoreInBox(callback)
    }

}