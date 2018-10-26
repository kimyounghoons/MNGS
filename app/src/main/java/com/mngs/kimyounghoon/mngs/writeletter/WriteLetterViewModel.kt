package com.mngs.kimyounghoon.mngs.writeletter

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.utils.TimeHelper
import java.util.*

class WriteLetterViewModel(context: Application, private val lettersRepository: LettersRepository) : AndroidViewModel(context), LettersDataSource.SendLetterCallback {

    val title = ObservableField<String>()
    val content = ObservableField<String>()
    val completed = ObservableBoolean()
    val sendLetterCommand = SingleLiveEvent<Void>()
    val snackbarMessage = SingleLiveEvent<Int>()

    override fun onLetterSended() {
        showSnackbarMessage(R.string.sended_letter)
    }

    override fun onFailedToSendLetter() {
        showSnackbarMessage(R.string.failed_to_send_letter)
    }

    fun sendLetter() {
        lettersRepository.sendLetter(Letter(lettersRepository.getId(),FirebaseAuth.getInstance().currentUser!!.uid,false,title.get()!!,content.get()!!, TimeHelper.getCurrentTime()),this)
        sendLetterCommand.call()
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
    }

}