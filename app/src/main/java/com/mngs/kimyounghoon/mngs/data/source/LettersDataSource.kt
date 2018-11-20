package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.data.User

interface LettersDataSource {

    interface UserCallback{
        fun onSuccess(user: User)

        fun onFail()
    }

    interface SignupCallback{
        fun onSuccess()

        fun onFail()
    }

    interface LoadLettersCallback {
        fun onLettersLoaded(letters: List<Letter>)

        fun onFailedToLoadLetters()
    }

    interface LoadMoreLettersCallback {
        fun onLettersMoreLoaded(letters: List<Letter>)

        fun onFailedToLoadMoreLetters()
    }

    interface LoadAnswersCallback {
        fun onAnswersLoaded(answers: List<Answer>)

        fun onFailedToLoadAnswers()
    }

    interface LoadMoreAnswersCallback {
        fun onAnswersMoreLoaded(answers: List<Answer>)

        fun onFailedToLoadMoreAnswers()
    }

    interface LoadReAnswersCallback {
        fun onReAnswersLoaded(reAnswers: List<ReAnswer>)

        fun onFailedToLoadReAnswers()
    }

    interface LoadMoreReAnswersCallback {
        fun onReAnswersMoreLoaded(reAnswers: List<ReAnswer>)

        fun onFailedToLoadMoreReAnswers()
    }


    interface GetLetterCallback {
        fun onLetterLoaded(letter: Letter)

        fun onFailedToLoadLetters()
    }

    interface SendLetterCallback {
        fun onLetterSended()

        fun onFailedToSendLetter()
    }

    interface SendAnswerCallback {
        fun onAnswerSended()

        fun onFailedToSendAnswer()
    }

    fun loadLetters(callback: LoadLettersCallback)

    fun loadMoreLetters(callback: LoadMoreLettersCallback)

    fun loadInBox(callback: LoadLettersCallback)

    fun loadMoreInBox(callback: LoadMoreLettersCallback)

    fun loadAnswers(letterId: String,callback: LettersDataSource.LoadAnswersCallback)

    fun loadMoreAnswers(callback: LettersDataSource.LoadMoreAnswersCallback)

    fun getLetter(letterId: String, callBack: GetLetterCallback)

    fun sendLetter(letter: Letter, callBack: SendLetterCallback)

    fun answerLetter(answer: Answer, callBack: SendAnswerCallback)

    fun sendReAnswer(reAnswer: ReAnswer, callback: SendLetterCallback)

    fun getLetterId(): String

    fun getAnswerId(): String

    fun getReAnswerId(): String

    fun loadReAnswers(letterId: String, callback: LoadReAnswersCallback)

    fun loadMoreReAnswers(callback: LoadMoreReAnswersCallback)

    fun loadSentAnswers(callback: LettersDataSource.LoadAnswersCallback)

    fun loadMoreSentAnswers(callback: LoadMoreAnswersCallback)

    fun signup(callback : SignupCallback)

    fun getUser(userId : String, callback: UserCallback)

    fun sendRefreshToken(token : String)

}