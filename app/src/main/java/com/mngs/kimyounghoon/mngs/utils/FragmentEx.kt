package com.mngs.kimyounghoon.mngs.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.mngs.kimyounghoon.mngs.ViewModelFactory


fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(viewModelClass)