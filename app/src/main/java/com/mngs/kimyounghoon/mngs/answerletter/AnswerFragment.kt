package com.mngs.kimyounghoon.mngs.answerletter

import android.app.FragmentManager
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.HOME
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentAnswerLetterBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class AnswerFragment : AbstractFragment() {
    override fun getTitle(): String {
        return getString(R.string.answer_letter)
    }

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
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answer_letter, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }

        obtainViewModel().let {
            it.sentLetterCommand.observe(this, Observer {
                if (context is FragmentActivity) {
                    (context as FragmentActivity).supportFragmentManager.popBackStackImmediate(HOME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }

    private fun obtainViewModel(): AnswerViewModel = obtainViewModel(AnswerViewModel::class.java)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_letter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_write_letter -> {
            binding.viewModel?.sendAnswer(letter)
            true
        }
        else -> {
            false
        }
    }
}