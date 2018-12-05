package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.*

interface LettersDataSource {

    interface VersionCallback{
        fun onSuccess(version: Version)

        fun onFailedToGetVersion()
    }

    interface UserCallback{
        fun onSuccess(user: User)

        fun onFailToGetUser()
    }

    interface SignupCallback{
        fun onSuccess()

        fun onFail()
    }

    interface LoadItemsCallback {
        fun onLoaded(items: List<AbstractId>)
        fun onFailedToLoadItems()
    }

    interface LoadMoreItemsCallback {
        fun onMoreLoaded(items: List<AbstractId>)
        fun onFailedToLoadMoreItems()
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

    fun loadLetters(callback: LoadItemsCallback)

    fun loadMoreLetters(callback: LoadMoreItemsCallback)

    fun loadInBox(callback: LoadItemsCallback)

    fun loadMoreInBox(callback: LoadMoreItemsCallback)

    fun loadAnswers(letterId: String,callback: LettersDataSource.LoadItemsCallback)

    fun loadMoreAnswers(callback: LettersDataSource.LoadMoreItemsCallback)

    fun loadReAnswers(letterId: String, callback: LoadItemsCallback)

    fun loadMoreReAnswers(callback: LoadMoreItemsCallback)

    fun loadSentAnswers(callback: LettersDataSource.LoadItemsCallback)

    fun loadMoreSentAnswers(callback: LoadMoreItemsCallback)

    fun getLetter(letterId: String, callBack: GetLetterCallback)

    fun sendLetter(letter: Letter, callBack: SendLetterCallback)

    fun answerLetter(answer: Answer, callBack: SendAnswerCallback)

    fun sendReAnswer(reAnswer: ReAnswer, callback: SendLetterCallback)

    fun getLetterId(): String

    fun getAnswerId(): String

    fun getReAnswerId(): String

    fun signup(callback : SignupCallback)

    fun getUser(userId : String, callback: UserCallback)

    fun sendRefreshToken(token : String)

    fun checkVersion(callback: LettersDataSource.VersionCallback)

}