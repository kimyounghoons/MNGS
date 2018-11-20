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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mngs.kimyounghoon.mngs.data.User
import com.mngs.kimyounghoon.mngs.data.source.LettersDataSource
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource
import com.mngs.kimyounghoon.mngs.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var locateListener: LocateListener? = null
    private lateinit var fragmentSplashBinding: FragmentSplashBinding
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var reference : LettersDataSource
    private var isLogin: Boolean = false
    private var auth: FirebaseAuth? = null

    companion object {
        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        auth?.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        auth?.removeAuthStateListener { mAuthListener }
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
        auth = FirebaseAuth.getInstance()
        reference = LettersFirebaseDataSource
        mAuthListener = FirebaseAuth.AuthStateListener {
            isLogin = true
        }
    }


    private fun tryHome() {
        reference.getUser(auth?.uid!!, object : LettersDataSource.UserCallback {
            override fun onSuccess(user: User) {
                locateListener?.openHome()
            }

            override fun onFail() {
              locateListener?.openLogin()
            }

        })
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
            showActionBar()
            if (isLogin) {
                val user: FirebaseUser? = auth?.currentUser
                if (user != null) {
                    tryHome()
                } else {
                    locateListener?.openLogin()
                }
            } else {
                locateListener?.openLogin()
            }
        }, 3000)
        return fragmentSplashBinding.root
    }

}