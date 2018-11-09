package com.mngs.kimyounghoon.mngs.reanswers

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentReanswersBinding
import kotlinx.android.synthetic.main.fragment_reanswers.*

class ReAnswersFragment : AbstractFragment() {
    lateinit var letter: Letter
    lateinit var answer: Answer
    lateinit var binding: FragmentReanswersBinding

    override fun getTitle(): String = getString(R.string.answers)


    companion object {
        private const val KEY_JSON_ANSWER = "KEY_JSON_ANSWER"
        private const val KEY_JSON_LETTER = "KEY_JSON_LETTER"

        fun newInstance(jsonLetter: String, jsonAnswer: String): ReAnswersFragment {
            val reAnswersFragment = ReAnswersFragment()
            val bundle = Bundle()
            bundle.putString(KEY_JSON_LETTER, jsonLetter)
            bundle.putString(KEY_JSON_ANSWER, jsonAnswer)
            reAnswersFragment.arguments = bundle
            return reAnswersFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.setTitle(getTitle())

        val jsonLetter = arguments?.getString(ReAnswersFragment.KEY_JSON_LETTER)
                ?: throw Exception("must be set jsonLetter !!")
        letter = Gson().fromJson(jsonLetter, Letter::class.java)

        val jsonAnswer = arguments?.getString(ReAnswersFragment.KEY_JSON_ANSWER)
                ?: throw Exception("must be set jsonAnswer !!")
        answer = Gson().fromJson(jsonAnswer, Answer::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reanswers, container, false)
        binding.apply {
            letterId.text = letter.letterId
            letterContent.text = letter.content
            answerId.text = answer.answerId
            answerContent.text = answer.content
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_reanswer, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_reanswer -> {
            locateListener?.openReAnswer(answer)
            true
        }
        else -> {
            false
        }
    }
}