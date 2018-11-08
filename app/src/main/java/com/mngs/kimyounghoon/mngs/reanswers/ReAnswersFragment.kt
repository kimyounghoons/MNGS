package com.mngs.kimyounghoon.mngs.reanswers

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.answerletter.AnswerFragment
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentReanswersBinding

class ReAnswersFragment : AbstractFragment() {
    lateinit var answer: Answer
    lateinit var binding : FragmentReanswersBinding

    override fun getTitle(): String = getString(R.string.answers)


    companion object {
        private const val KEY_JSON_ANSWER = "KEY_JSON_ANSWER"

        fun newInstance(jsonAnswer: String): ReAnswersFragment {
            val reAnswersFragment = ReAnswersFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_ANSWER, jsonAnswer)
            reAnswersFragment.arguments = bundle
            return reAnswersFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.setTitle(getTitle())
        val jsonAnswer = arguments?.getString(ReAnswersFragment.KEY_JSON_ANSWER)
                ?: throw Exception("must be set jsonAnswer !!")
        answer = Gson().fromJson(jsonAnswer, Answer::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reanswers, container, false)
        binding.apply {

        }
        return binding.root
    }
}