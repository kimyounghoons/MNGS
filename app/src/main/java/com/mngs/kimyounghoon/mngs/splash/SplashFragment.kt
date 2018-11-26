package com.mngs.kimyounghoon.mngs.splash

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentSplashBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

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

    private fun hideActionBar() {
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).supportActionBar?.hide()
        }
    }

    private fun showActionBar() {
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).supportActionBar?.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSplashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        Handler().postDelayed({
            obtainViewModel().popupMessage.observe(this, Observer {

            })
            obtainViewModel().checkVersion()
            showActionBar()
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                locateListener?.openHome()
            } else {
                locateListener?.openLogin()
            }
        }, 3000)
        return fragmentSplashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }


    private fun obtainViewModel(): SplashViewModel = obtainViewModel(SplashViewModel::class.java)
}