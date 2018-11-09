package com.mngs.kimyounghoon.mngs.reanswer

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
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.databinding.FragmentReanswerBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class ReAnswerFragment : AbstractFragment() {
    override fun getTitle(): String {
        return getString(R.string.reanswer)
    }

    lateinit var binding: FragmentReanswerBinding
    lateinit var answer : Answer

    companion object {
        private const val KEY_JSON_ANSWER_TO_REANSWER = "KEY_JSON_ANSWER_TO_REANSWER"

        fun newInstance(jsonAnswer: String): ReAnswerFragment {
            val reAnswerFragment = ReAnswerFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_ANSWER_TO_REANSWER, jsonAnswer)
            reAnswerFragment.arguments = bundle
            return reAnswerFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.setTitle(getTitle())
        val jsonAnswer = arguments?.getString(KEY_JSON_ANSWER_TO_REANSWER)
                ?: throw Exception("must be set jsonAnswer!!")
        answer = Gson().fromJson(jsonAnswer, Answer::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reanswer, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }

        obtainViewModel().let {
            it.sendLetterCommand.observe(this, Observer {
                Toast.makeText(context, "StartToSendLetter", Toast.LENGTH_SHORT).show()
            })
            it.sentLetterCommand.observe(this, Observer {
                if (context is FragmentActivity) {
                    (context as FragmentActivity).supportFragmentManager.popBackStackImmediate(Constants.HOME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    private fun obtainViewModel(): ReAnswerViewModel = obtainViewModel(ReAnswerViewModel::class.java)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_letter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_write_letter -> {
            binding.viewModel?.sendReAnswer(answer)
            true
        }
        else -> {
            false
        }
    }
}