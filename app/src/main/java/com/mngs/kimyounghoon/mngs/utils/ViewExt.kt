package com.mngs.kimyounghoon.mngs.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import com.mngs.kimyounghoon.mngs.ProgressDialogFragment
import com.mngs.kimyounghoon.mngs.SingleLiveEvent

fun View.setupToast(lifecycleOwner: LifecycleOwner,
                    toastMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    toastMessageLiveEvent.observe(lifecycleOwner, Observer { it ->
        it?.let { showToast(context.getString(it), timeLength) }
    })
}

fun View.showToast(snackbarText: String, timeLength: Int) {
    Toast.makeText(context, snackbarText, timeLength).show()
}

fun View.setupProgressDialog(lifecycleOwner: LifecycleOwner,
                             progressLiveEvent: SingleLiveEvent<Boolean>) {
    progressLiveEvent.observe(lifecycleOwner, Observer { it ->
        it?.let {
            if (it) {
                showDialog()
            } else {
                dismissDialog()
            }
        }
    })
}

fun View.showDialog() {
    if (context is FragmentActivity) {
        val fm = (context as FragmentActivity).supportFragmentManager
        val progressDialogFragment = ProgressDialogFragment()
        progressDialogFragment.show(fm, "progress")
    }
}

fun View.dismissDialog() {
    if (context is FragmentActivity) {
        val progressDialogFragment = (context as FragmentActivity).supportFragmentManager.findFragmentByTag("progress") as ProgressDialogFragment?
        progressDialogFragment?.dismiss()
    }
}