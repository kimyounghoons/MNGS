package com.mngs.kimyounghoon.mngs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

abstract class AbstractFragment : Fragment() {
    protected var locateListener: LocateListener? = null
    protected var actionBarListener: ActionBarListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LocateListener) {
            locateListener = context
        }

        if (context is ActionBarListener) {
            actionBarListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        locateListener = null
        actionBarListener = null
    }

    abstract fun getTitle(): String

    fun hideActionBar() {
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).supportActionBar?.hide()
        }
    }

    fun showActionBar() {
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).supportActionBar?.show()
        }
    }

}