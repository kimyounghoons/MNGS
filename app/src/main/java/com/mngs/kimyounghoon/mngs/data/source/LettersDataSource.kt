package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Letter

interface LettersDataSource {

    interface LoadLettersCallback {
        fun onLettersLoaded(letters: List<Letter>)

        fun onFailedToLoadLetters()
    }

    interface GetLetterCallback {
        fun onLetterLoaded(letter: Letter)

        fun onFailedToLoadLetters()
    }

    interface SendLetterCallback {
        fun onLetterSended()

        fun onFailedToSendLetter()
    }

    fun loadLetters(callback: LoadLettersCallback)

    fun getLetter(letterId: String, callBack: GetLetterCallback)

    fun sendLetter(letter: Letter, callBack: SendLetterCallback)

    fun getId() : String

}