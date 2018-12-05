package com.mngs.kimyounghoon.mngs.login

import android.arch.lifecycle.ViewModel
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.SingleLiveEvent
import com.mngs.kimyounghoon.mngs.data.User
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class LoginViewModel(private val lettersRepository: LettersRepository) : ViewModel(), LettersDataSource.UserCallback, LoginNavigator {

    var needProgress = SingleLiveEvent<Boolean>()
    var onFacebookLoginCommand = SingleLiveEvent<Void>()
    var onGoogleLoginCommand = SingleLiveEvent<Void>()
    var locateListener: LocateListener? = null

    override fun onFacebookLogin() {
        showProgress()
        onFacebookLoginCommand.call()
    }

    override fun onGoogleLogin() {
        showProgress()
        onGoogleLoginCommand.call()
    }

    fun tryHome(userId: String, locateListener: LocateListener?) {
        this.locateListener = locateListener
        lettersRepository.getUser(userId, this)
    }

    override fun onSuccess(user: User) {
        dismissProgress()
        locateListener?.openHome()
    }

    override fun onFailToGetUser() { // getUser Fail
        lettersRepository.signup(object : LettersDataSource.SignupCallback {
            override fun onSuccess() {
                //가입 성공
                dismissProgress()
                locateListener?.openHome()
            }

            override fun onFail() {
                dismissProgress()
                //가입 실패
            }

        })
    }

    fun showProgress() {
        needProgress.value = true
    }

    fun dismissProgress() {
        needProgress.value = false
    }

}