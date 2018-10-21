package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Letter

interface LettersDataSource {

    interface LoadLettersCallback {
        fun onLettersLoaded(letters: List<Letter>)
    }

    interface GetLetterCallback {
        fun onLetterLoaded(letter: Letter)
    }

    interface SendLetterCallback {
        fun onLetterSended(letter: Letter)
    }

    fun getLetters(callback: LoadLettersCallback)

    fun getLetter(letterId: String, callBack: GetLetterCallback)

    fun sendLetter(letter: Letter)
}