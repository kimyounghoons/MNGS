package com.mngs.kimyounghoon.mngs

import android.content.Context
import android.support.v4.app.Fragment

abstract class AbstractFragment : Fragment() {
    protected var locateListener: LocateListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LocateListener) {
            locateListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        locateListener = null
    }

}