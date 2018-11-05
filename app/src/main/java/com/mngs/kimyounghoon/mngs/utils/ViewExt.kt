package com.mngs.kimyounghoon.mngs.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.view.View
import android.widget.Toast
import com.mngs.kimyounghoon.mngs.SingleLiveEvent


/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupToast(lifecycleOwner: LifecycleOwner,
                    toastMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    toastMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showToast(context.getString(it), timeLength) }
    })
}

fun View.showToast(snackbarText: String, timeLength: Int) {
    Toast.makeText(context, snackbarText, timeLength).show()
}