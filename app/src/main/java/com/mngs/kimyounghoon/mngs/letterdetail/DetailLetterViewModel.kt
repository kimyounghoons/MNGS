package com.mngs.kimyounghoon.mngs.letterdetail

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mngs.kimyounghoon.mngs.data.Letter

class DetailLetterViewModel(val letter: Letter, val isOwnLetter: Boolean) : ViewModel() {
    var title = ObservableField<String>(letter.title)
    var content = ObservableField<String>(letter.content)
    var time = ObservableField<Long>(letter.time)
}