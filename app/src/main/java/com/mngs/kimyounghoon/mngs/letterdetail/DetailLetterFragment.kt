package com.mngs.kimyounghoon.mngs.letterdetail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentDetailLetterBinding

class DetailLetterFragment : AbstractFragment() {
    lateinit var letterId: String
    lateinit var title: String
    lateinit var content: String
    lateinit var binding: FragmentDetailLetterBinding
    lateinit var date: String
    lateinit var letter: Letter

    companion object {
        private const val KEY_JSON_LETTER = "KEY_JSON_LETTER"

        fun newInstance(jsonLetter: String): DetailLetterFragment {
            val letterDetailFragment = DetailLetterFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_LETTER, jsonLetter)
            letterDetailFragment.arguments = bundle
            return letterDetailFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonLetter = arguments?.getString(KEY_JSON_LETTER)
                ?: throw Exception("must be set jsonLetter !!")
        letter = Gson().fromJson(jsonLetter, Letter::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_letter, container, false)
        binding.apply {
            viewModel = DetailLetterViewModel(letter, letter.userId.equals(FirebaseAuth.getInstance().uid))
            userActionListener = locateListener
        }
        return binding.root
    }
}