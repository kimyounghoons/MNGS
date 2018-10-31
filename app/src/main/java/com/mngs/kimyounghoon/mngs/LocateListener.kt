package com.mngs.kimyounghoon.mngs

import com.mngs.kimyounghoon.mngs.data.Letter

interface LocateListener{
    fun openLogin()

    fun openSplash()

    fun openHome()

    fun openSignup()

    fun openLetterDetail(letter:Letter)
}