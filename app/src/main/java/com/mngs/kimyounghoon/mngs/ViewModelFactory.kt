package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.mngs.kimyounghoon.mngs.answerletter.AnswerViewModel
import com.mngs.kimyounghoon.mngs.answers.AnswersViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.letters.LettersViewModel
import com.mngs.kimyounghoon.mngs.login.LoginViewModel
import com.mngs.kimyounghoon.mngs.myinbox.MyInBoxViewModel
import com.mngs.kimyounghoon.mngs.reanswer.ReAnswerViewModel
import com.mngs.kimyounghoon.mngs.reanswers.ReAnswersViewModel
import com.mngs.kimyounghoon.mngs.sentanswers.SentAnswersViewModel
import com.mngs.kimyounghoon.mngs.splash.SplashViewModel
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterViewModel

class ViewModelFactory private constructor(
        private val lettersRepository: LettersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(WriteLetterViewModel::class.java) ->
                        WriteLetterViewModel(lettersRepository)
                    isAssignableFrom(LettersViewModel::class.java) ->
                        LettersViewModel(lettersRepository)
                    isAssignableFrom(MyInBoxViewModel::class.java) ->
                        MyInBoxViewModel(lettersRepository)
                    isAssignableFrom(AnswerViewModel::class.java) ->
                        AnswerViewModel(lettersRepository)
                    isAssignableFrom(AnswersViewModel::class.java) ->
                        AnswersViewModel(lettersRepository)
                    isAssignableFrom(ReAnswerViewModel::class.java) ->
                        ReAnswerViewModel(lettersRepository)
                    isAssignableFrom(ReAnswersViewModel::class.java) ->
                        ReAnswersViewModel(lettersRepository)
                    isAssignableFrom(SentAnswersViewModel::class.java) ->
                        SentAnswersViewModel(lettersRepository)
                    isAssignableFrom(SplashViewModel::class.java) ->
                        SplashViewModel(lettersRepository)
                    isAssignableFrom(LoginViewModel::class.java) ->
                        LoginViewModel(lettersRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(LettersRepository.getInstance(LettersFirebaseDataSource))
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}