package com.mngs.kimyounghoon.mngs.answerletter

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentAnswerLetterBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class AnswerFragment : AbstractFragment() {

    lateinit var binding: FragmentAnswerLetterBinding
    lateinit var letter: Letter

    companion object {
        private const val KEY_JSON_LETTER_TO_ANSWER = "KEY_JSON_LETTER_TO_ANSWER"

        fun newInstance(jsonLetter: String): AnswerFragment {
            val answerFragment = AnswerFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_LETTER_TO_ANSWER, jsonLetter)
            answerFragment.arguments = bundle
            return answerFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonLetter = arguments?.getString(AnswerFragment.KEY_JSON_LETTER_TO_ANSWER)
                ?: throw Exception("must be set jsonLetter !!")
        letter = Gson().fromJson(jsonLetter, Letter::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answer_letter, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        binding.sendButton.setOnClickListener {
            binding.viewModel?.sendAnswer(letter)
        }

        obtainViewModel().let {
            it.sendLetterCommand.observe(this, Observer {
                Toast.makeText(context, "StartToSendLetter", Toast.LENGTH_LONG).show()
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_LONG)
        }
    }

    private fun obtainViewModel(): AnswerViewModel = obtainViewModel(AnswerViewModel::class.java)

}