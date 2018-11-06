package com.mngs.kimyounghoon.mngs.letterdetail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentDetailLetterBinding

class DetailLetterFragment : AbstractFragment() {

    override fun getTitle(): String {
        return getString(R.string.detail_letter)
    }

    lateinit var binding: FragmentDetailLetterBinding
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
        if (letter.userId != FirebaseAuth.getInstance().uid)
            setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_letter, container, false)
        binding.apply {
            viewModel = DetailLetterViewModel(letter, letter.userId.equals(FirebaseAuth.getInstance().uid))
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail_letter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_answer_letter -> {
            locateListener?.openAnswer(letter)
            true
        }
        else -> {
            false
        }
    }

}