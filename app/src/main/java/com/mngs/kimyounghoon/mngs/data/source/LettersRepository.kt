package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer

class LettersRepository(private val lettersDataSource: LettersDataSource) : LettersDataSource {
    override fun checkVersion(callback: LettersDataSource.VersionCallback) {
        lettersDataSource.checkVersion(callback)
    }

    override fun sendRefreshToken(token: String) {
        lettersDataSource.sendRefreshToken(token)
    }

    override fun getUser(userId: String, callback: LettersDataSource.UserCallback) {
        lettersDataSource.getUser(userId, callback)
    }

    override fun signup(callback: LettersDataSource.SignupCallback) {
        lettersDataSource.signup(callback)
    }

    override fun sendReAnswer(reAnswer: ReAnswer, callback: LettersDataSource.SendLetterCallback) {
        lettersDataSource.sendReAnswer(reAnswer, callback)
    }

    override fun getReAnswerId(): String {
        return lettersDataSource.getReAnswerId()
    }

    override fun getAnswerId(): String {
        return lettersDataSource.getAnswerId()
    }

    override fun answerLetter(answer: Answer, callBack: LettersDataSource.SendAnswerCallback) {
        lettersDataSource.answerLetter(answer, callBack)
    }

    override fun getLetterId(): String {
        return lettersDataSource.getLetterId()
    }

    override fun loadLetters(callback: LettersDataSource.LoadItemsCallback) {
        lettersDataSource.loadLetters(callback)
    }

    override fun loadMoreLetters(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersDataSource.loadMoreLetters(callback)
    }

    override fun loadInBox(callback: LettersDataSource.LoadItemsCallback) {
        lettersDataSource.loadInBox(callback)
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersDataSource.loadMoreInBox(callback)
    }

    override fun loadAnswers(letterId: String, callback: LettersDataSource.LoadItemsCallback) {
        lettersDataSource.loadAnswers(letterId, callback)
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersDataSource.loadMoreAnswers(callback)
    }

    override fun loadReAnswers(letterId: String, callback: LettersDataSource.LoadItemsCallback) {
        lettersDataSource.loadReAnswers(letterId, callback)
    }

    override fun loadMoreReAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersDataSource.loadMoreReAnswers(callback)
    }

    override fun loadSentAnswers(callback: LettersDataSource.LoadItemsCallback) {
        lettersDataSource.loadSentAnswers(callback)
    }

    override fun loadMoreSentAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        lettersDataSource.loadMoreSentAnswers(callback)
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {
        lettersDataSource.getLetter(letterId, callBack)
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