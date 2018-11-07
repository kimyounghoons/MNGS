package com.mngs.kimyounghoon.mngs.writeletter

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.utils.TimeHelper
import kotlin.math.acosh

class WriteLetterViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.SendLetterCallback {

    val title = ObservableField<String>()
    val content = ObservableField<String>()
    val completed = SingleLiveEvent<Boolean>()
    val sendLetterCommand = SingleLiveEvent<Void>()
    val toastMessage = SingleLiveEvent<Int>()

    override fun onLetterSended() {
        showToastMessage(R.string.sended_letter)
        title.set(EMPTY)
        content.set(EMPTY)
        completed.call()
    }

    override fun onFailedToSendLetter() {
        showToastMessage(R.string.failed_to_send_letter)
    }

    fun sendLetter() {
        lettersRepository.sendLetter(Letter(lettersRepository.getLetterId(), FirebaseAuth.getInstance().currentUser!!.uid, false, title.get()?:EMPTY, content.get()?: EMPTY, TimeHelper.getCurrentTime()), this)
        sendLetterCommand.call()
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}