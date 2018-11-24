package com.mngs.kimyounghoon.mngs.splash

import android.arch.lifecycle.ViewModel
import com.mngs.kimyounghoon.mngs.BuildConfig
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.Version
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class SplashViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.VersionCallback {

    val toastMessage = SingleLiveEvent<Int>()
    val popupMessage = SingleLiveEvent<Int>()

    fun checkVersion() {
        lettersRepository.checkVersion(this)
    }

    override fun onSuccess(version: Version) {
        if(version.minVersion!= BuildConfig.VERSION_NAME){

        }
    }

    override fun onFailedToGetVersion() {

    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }

    private fun showDialogMessage(message: Int){
        popupMessage.value =message
    }

    override fun onCleared() {
        super.onCleared()
    }
}