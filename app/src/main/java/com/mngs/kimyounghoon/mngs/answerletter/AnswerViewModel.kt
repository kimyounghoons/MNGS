package com.mngs.kimyounghoon.mngs.answerletter

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.utils.TimeHelper

class AnswerViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.SendAnswerCallback {

    val content = ObservableField<String>()
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
        lettersRepository.answerLetter(Answer(lettersRepository.getAnswerId(), letter.id, letter.userId, FirebaseAuth.getInstance().currentUser!!.uid, false,content.get()?:EMPTY, TimeHelper.getCurrentTime()), this)
        showToastMessage(R.string.sending_answer)
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}