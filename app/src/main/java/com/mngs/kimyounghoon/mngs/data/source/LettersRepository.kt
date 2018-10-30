package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Letter

class LettersRepository(val lettersDataSource: LettersDataSource) : LettersDataSource {

    override fun getId() : String {
        return lettersDataSource.getId()
    }
    override fun loadLetters(callback: LettersDataSource.LoadLettersCallback) {
        lettersDataSource.loadLetters(callback)
    }

    override fun loadMoreLetters(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersDataSource.loadMoreLetters(callback)
    }

    override fun loadInBox(callback: LettersDataSource.LoadLettersCallback) {
        lettersDataSource.loadInBox(callback)
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersDataSource.loadMoreInBox(callback)
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {

    }

    override fun sendLetter(letter: Letter, callback: LettersDataSource.SendLetterCallback) {
        lettersDataSource.sendLetter(letter, callback)
    }

    companion object {

        private var INSTANCE: LettersRepository? = null

        @JvmStatic
        fun getInstance(lettersDataSource: LettersDataSource) =
                INSTANCE ?: synchronized(LettersRepository::class.java) {
                    INSTANCE ?: LettersRepository(lettersDataSource)
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}