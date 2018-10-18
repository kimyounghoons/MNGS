package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.mngs.kimyounghoon.mngs.login.LoginViewModel

class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(LoginViewModel::class.java) ->
                        LoginViewModel(application)
//                    isAssignableFrom(TaskDetailViewModel::class.java) ->
//                        TaskDetailViewModel(application, tasksRepository)
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
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application)
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}