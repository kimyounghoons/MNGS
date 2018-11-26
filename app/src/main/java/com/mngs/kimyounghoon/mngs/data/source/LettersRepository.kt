package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer

class LettersRepository(private val lettersDataSource: LettersDataSource) : LettersDataSource {
    override fun checkVersion(callback: LettersDataSource.VersionCallback) {

    }

    override fun sendRefreshToken(token: String) {

    }

    override fun getUser(userId: String, callback: LettersDataSource.UserCallback) {

    }

    override fun signup(callback: LettersDataSource.SignupCallback) {

    }

    override fun sendReAnswer(reAnswer: ReAnswer, callback: LettersDataSource.SendLetterCallback) {
        lettersDataSource.sendReAnswer(reAnswer, callback)
    }

    override fun getReAnswerId(answerId : String): String {
        return lettersDataSource.getReAnswerId(answerId)
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

    override fun loadAnswers(letterId: String, callback: LettersDataSource.LoadAnswersCallback) {
        lettersDataSource.loadAnswers(letterId, callback)
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreAnswersCallback) {
        lettersDataSource.loadMoreAnswers(callback)
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {
        lettersDataSource.getLetter(letterId,callBack)
    }

    override fun sendLetter(letter: Letter, callback: LettersDataSource.SendLetterCallback) {
        lettersDataSource.sendLetter(letter, callback)
    }

    override fun loadReAnswers(letterId: String, callback: LettersDataSource.LoadReAnswersCallback) {
        lettersDataSource.loadReAnswers(letterId, callback)
    }

    override fun loadMoreReAnswers(callback: LettersDataSource.LoadMoreReAnswersCallback) {
        lettersDataSource.loadMoreReAnswers(callback)
    }

    override fun loadSentAnswers(callback: LettersDataSource.LoadAnswersCallback) {
        lettersDataSource.loadSentAnswers(callback)
    }

    override fun loadMoreSentAnswers(callback: LettersDataSource.LoadMoreAnswersCallback) {
        lettersDataSource.loadMoreSentAnswers(callback)
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