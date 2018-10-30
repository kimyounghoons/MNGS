package com.mngs.kimyounghoon.mngs.letterdetail

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class DetailLetterViewModel : ViewModel(){
    var title = ObservableField<String>()
    var content = ObservableField<String>()

}