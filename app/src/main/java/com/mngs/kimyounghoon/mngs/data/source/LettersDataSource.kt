package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter

interface LettersDataSource {

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

    fun getLetterId(): String

    fun getAnswerId(): String

}