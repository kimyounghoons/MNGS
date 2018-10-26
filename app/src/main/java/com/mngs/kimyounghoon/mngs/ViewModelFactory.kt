package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.letters.LettersViewModel
import com.mngs.kimyounghoon.mngs.myinbox.MyInBoxViewModel
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterViewModel

class ViewModelFactory private constructor(
        private val application: Application, private val lettersRepository: LettersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(WriteLetterViewModel::class.java) ->
                        WriteLetterViewModel(application, lettersRepository)
                    isAssignableFrom(LettersViewModel::class.java) ->
                        LettersViewModel(application, lettersRepository)
                    isAssignableFrom(MyInBoxViewModel::class.java) ->
                        MyInBoxViewModel(application, lettersRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application, LettersRepository.getInstance(LettersFirebaseDataSource))
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}