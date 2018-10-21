package com.mngs.kimyounghoon.mngs.writeletter

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class WriteViewModel(context: Application, private val lettersRepository: LettersRepository) : AndroidViewModel(context), LettersDataSource.SendLetterCallback {

    val completed = ObservableBoolean()
    val sendLetterCommand = SingleLiveEvent<Void>()
    val snackbarMessage = SingleLiveEvent<Void>()

    override fun onLetterSended(letter: Letter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun sendLetter() {
        sendLetterCommand.call()
    }

}