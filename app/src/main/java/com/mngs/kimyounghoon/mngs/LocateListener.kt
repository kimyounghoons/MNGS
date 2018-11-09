package com.mngs.kimyounghoon.mngs

import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter

interface LocateListener{
    fun openLogin()

    fun openSplash()

    fun openHome()

    fun openSignup()

    fun openLetterDetail(letter:Letter)

    fun openAnswer(letter: Letter)

    fun openAnswers(letter: Letter)

    fun openReAnswer(answer : Answer)

    fun openReAnswers(letter : Letter , answer: Answer)
}