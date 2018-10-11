package com.mngs.kimyounghoon.mngs

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var locateListener: LocateListener? = null
    private lateinit var fragmentSplashBinding: FragmentSplashBinding

    companion object {
        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSplashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        Handler().postDelayed({
            locateListener?.openLogin()
            showActionBar()
        }, 3000)
        return fragmentSplashBinding.root
    }

}