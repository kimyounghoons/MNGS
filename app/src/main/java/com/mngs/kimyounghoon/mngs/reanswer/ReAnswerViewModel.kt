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
    val needProgress = SingleLiveEvent<Boolean>()

    override fun onLetterSended() {
        needProgress.value = false
        showToastMessage(R.string.sended_answer_letter)
        sentLetterCommand.call()
    }

    override fun onFailedToSendLetter() {
        needProgress.value = false
        showToastMessage(com.mngs.kimyounghoon.mngs.R.string.failed_to_send_answer_letter)
    }

    fun sendReAnswer(answer: Answer) {
        needProgress.value = true
        lettersRepository.sendReAnswer(ReAnswer(lettersRepository.getReAnswerId(), answer.id, answer.letterId, answer.originUserId, answer.answerUserId, content.get()
                ?: Constants.EMPTY, TimeHelper.getCurrentTime()), this)
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

}