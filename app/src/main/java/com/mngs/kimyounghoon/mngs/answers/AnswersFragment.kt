package com.mngs.kimyounghoon.mngs.answers

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentAnswersBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class AnswersFragment : AbstractFragment() {
    lateinit var letter: Letter
    lateinit var binding: FragmentAnswersBinding
    private lateinit var adapter: AnswersAdapter

    override fun getTitle(): String = getString(R.string.answers)


    companion object {
        private const val KEY_JSON_LETTER = "KEY_JSON_LETTER"

        fun newInstance(jsonLetter: String): AnswersFragment {
            val answersFragment = AnswersFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_LETTER, jsonLetter)
            answersFragment.arguments = bundle
            return answersFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.setTitle(getTitle())
        val jsonLetter = arguments?.getString(KEY_JSON_LETTER)
                ?: throw Exception("must be set jsonLetter !!")
        letter = Gson().fromJson(jsonLetter, Letter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        binding.viewModel?.start(letter.letterId)
    }

    private fun setUpAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            adapter = AnswersAdapter(locateListener!!)
            binding.recyclerview.adapter = adapter
        } else {
            Log.w("", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answers, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }

    fun obtainViewModel(): AnswersViewModel = obtainViewModel(AnswersViewModel::class.java)
}