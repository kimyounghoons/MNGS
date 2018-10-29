package com.mngs.kimyounghoon.mngs

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean

abstract class RecyclerBaseViewModel : ViewModel(){
    val isAllLoaded = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)

    fun start() {
        loadLetters(false)
    }

    fun loadLetters(forceUpdate: Boolean) {
        loadLetters(forceUpdate, true)
    }

    abstract fun loadLetters(forceUpdate: Boolean , showLoadingUI: Boolean)

    abstract fun loadMoreLetters()

}