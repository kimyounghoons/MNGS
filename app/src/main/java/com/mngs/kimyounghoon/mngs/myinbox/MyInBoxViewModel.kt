package com.mngs.kimyounghoon.mngs.myinbox

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.mngs.kimyounghoon.mngs.data.source.LettersRepository

class MyInBoxViewModel(context: Application, private val lettersRepository: LettersRepository) : AndroidViewModel(context) {

}