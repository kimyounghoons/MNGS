package com.mngs.kimyounghoon.mngs.signup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentSignupBinding

class SignupFragment : AbstractFragment() {
    lateinit var fragmentSignupBinding: FragmentSignupBinding
    private lateinit var databaseReference: DatabaseReference
    private var auth: FirebaseAuth? = null

    companion object {
        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }

    override fun getTitle(): String {
        return getString(R.string.signup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSignupBinding = (DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false) as FragmentSignupBinding).apply {
            fragment = this@SignupFragment
        }
        return fragmentSignupBinding.root
    }

    fun clickedConfirm() {
        if (fragmentSignupBinding.editNickname.textSize > 0) {
            databaseReference.child("users").child(auth?.currentUser!!.uid).setValue(fragmentSignupBinding.editNickname.text.toString()).addOnSuccessListener {
                locateListener?.openHome()
            }.addOnFailureListener {
                Toast.makeText(context, "다시 시도 해주세요.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, getString(R.string.insert_nickname), Toast.LENGTH_LONG).show()
        }
    }

}