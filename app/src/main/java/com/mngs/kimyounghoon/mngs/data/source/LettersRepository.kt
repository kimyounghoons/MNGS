package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter

class LettersRepository(private val lettersDataSource: LettersDataSource) : LettersDataSource {
    override fun getAnswerId(): String {
        return lettersDataSource.getAnswerId()
    }

    override fun answerLetter(answer: Answer, callBack: LettersDataSource.SendAnswerCallback) {
        lettersDataSource.answerLetter(answer, callBack)
    }

    override fun getLetterId(): String {
        return lettersDataSource.getLetterId()
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

    override fun loadAnswers(callback: LettersDataSource.LoadLettersCallback) {
        lettersDataSource.loadAnswers(callback)
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersDataSource.loadMoreAnswers(callback)
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