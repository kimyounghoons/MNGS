package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository
import com.mngs.kimyounghoon.mngs.writeletter.WriteViewModel

class ViewModelFactory private constructor(
        private val application: Application, private val lettersRepository: LettersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(WriteViewModel::class.java) ->
                        WriteViewModel(application, lettersRepository)
//                    isAssignableFrom(AddEditTaskViewModel::class.java) ->
//                        AddEditTaskViewModel(application, tasksRepository)
//                    isAssignableFrom(TasksViewModel::class.java) ->
//                        TasksViewModel(application, tasksRepository)
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
                    INSTANCE ?: ViewModelFactory(application, LettersRepository.getInstance())
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}