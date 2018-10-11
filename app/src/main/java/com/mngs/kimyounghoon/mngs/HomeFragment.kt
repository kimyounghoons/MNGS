package com.mngs.kimyounghoon.mngs

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mngs.kimyounghoon.mngs.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private var locateListener: LocateListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        fragmentHomeBinding.nameText.text = auth.currentUser?.displayName
        fragmentHomeBinding.emailText.text = auth.currentUser?.email
        fragmentHomeBinding.logoutButton.setOnClickListener {
            auth.signOut()
            LoginManager.getInstance().logOut()
            locateListener?.openLogin()
        }
        return fragmentHomeBinding.root
    }
}