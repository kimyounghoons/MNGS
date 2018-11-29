package com.mngs.kimyounghoon.mngs.splash

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.LocateListener
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.databinding.FragmentSplashBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupProgressDialog
import com.mngs.kimyounghoon.mngs.utils.setupToast

class SplashFragment : AbstractFragment() {
    override fun getTitle(): String {
        return EMPTY
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSplashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        Handler().postDelayed({
            obtainViewModel().checkVersion()
        }, 3000)

        obtainViewModel().popupMessage.observe(this, Observer {

        })
        obtainViewModel().tryLoginCommand.observe(this, Observer {
            it?.apply {
                if (it) {
                    showActionBar()
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        locateListener?.openHome()
                    } else {
                        locateListener?.openLogin()
                    }
                } else {
                    openMarket()
                }
            }
        })
        return fragmentSplashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
//            view.setupProgressDialog(this, it.needProgress)
        }
    }

    private fun obtainViewModel(): SplashViewModel = obtainViewModel(SplashViewModel::class.java)

    override fun onDetach() {
        super.onDetach()
        locateListener = null
    }

    private fun openMarket() {
        context?.apply {
            val appPackageName = packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
    }
}