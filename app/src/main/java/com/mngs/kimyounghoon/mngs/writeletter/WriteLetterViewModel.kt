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
    val toastMessage = SingleLiveEvent<Int>()
    val needProgress = SingleLiveEvent<Boolean>()

    override fun onLetterSended() {
        showToastMessage(R.string.sended_letter)
        title.set(EMPTY)
        content.set(EMPTY)
        needProgress.value = false
        completed.call()
    }

    override fun onFailedToSendLetter() {
        showToastMessage(R.string.failed_to_send_letter)
        needProgress.value = false
    }

    fun sendLetter() {
        needProgress.value = true
        lettersRepository.sendLetter(Letter(lettersRepository.getLetterId(), FirebaseAuth.getInstance().currentUser!!.uid, false, false,title.get()?:EMPTY, content.get()?: EMPTY, TimeHelper.getCurrentTime()), this)
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}