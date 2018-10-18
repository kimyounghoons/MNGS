package com.mngs.kimyounghoon.mngs

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.databinding.FragmentHomeBinding

class HomeFragment : AbstractFragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    //    private lateinit var auth: FirebaseAuth
//    private lateinit var storage: FirebaseStorage
//    private lateinit var database: FirebaseDatabase
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

//        auth = FirebaseAuth.getInstance()
//        storage = FirebaseStorage.getInstance()
//        database = FirebaseDatabase.getInstance()
//
//        fragmentHomeBinding.nameText.text = auth.currentUser?.displayName
//        fragmentHomeBinding.emailText.text = auth.currentUser?.email

//        fragmentHomeBinding.logoutButton.setOnClickListener {
//            auth.signOut()
//            LoginManager.getInstance().logOut()
//            locateListener?.openLogin()
//        }
        return fragmentHomeBinding.root
    }
}