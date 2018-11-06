package com.mngs.kimyounghoon.mngs.answerletter

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.utils.TimeHelper

class AnswerViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.SendAnswerCallback {

    val title = ObservableField<String>()
    val content = ObservableField<String>()
    val completed = ObservableBoolean()
    val sendLetterCommand = SingleLiveEvent<Void>()
    val sentLetterCommand = SingleLiveEvent<Void>()
    val toastMessage = SingleLiveEvent<Int>()

    override fun onAnswerSended() {
        showToastMessage(R.string.sended_answer_letter)
        sentLetterCommand.call()
    }

    override fun onFailedToSendAnswer() {
        showToastMessage(com.mngs.kimyounghoon.mngs.R.string.failed_to_send_answer_letter)
    }

    fun sendAnswer(letter: Letter) {
        lettersRepository.answerLetter(Answer(lettersRepository.getAnswerId(), letter.letterId, letter.userId, FirebaseAuth.getInstance().currentUser!!.uid, title.get()!!, content.get()!!, TimeHelper.getCurrentTime()), this)
        sendLetterCommand.call()
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}