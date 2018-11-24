package com.mngs.kimyounghoon.mngs.reanswer

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.ReAnswer
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.utils.TimeHelper

class ReAnswerViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.SendLetterCallback {

    val content = ObservableField<String>()
    val sentLetterCommand = SingleLiveEvent<Void>()
    val toastMessage = SingleLiveEvent<Int>()

    override fun onLetterSended() {
        showToastMessage(R.string.sended_answer_letter)
        sentLetterCommand.call()
    }

    override fun onFailedToSendLetter() {
        showToastMessage(com.mngs.kimyounghoon.mngs.R.string.failed_to_send_answer_letter)
    }

    fun sendReAnswer(answer : Answer) {
        lettersRepository.sendReAnswer(ReAnswer(lettersRepository.getReAnswerId(), answer.id, answer.letterId,answer.originUserId, answer.answerUserId, content.get()?: Constants.EMPTY, TimeHelper.getCurrentTime()), this)
        showToastMessage(R.string.sending_answer)
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}