package com.mngs.kimyounghoon.mngs.reanswers

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.google.gson.Gson
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.FragmentReanswersBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel

class ReAnswersFragment : AbstractFragment() {
    lateinit var letter: Letter
    lateinit var answer: Answer
    lateinit var binding: FragmentReanswersBinding
    lateinit var adapter: ReAnswersAdapter

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
            viewModel = obtainViewModel()
            fragment = this@ReAnswersFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        binding.viewModel?.start(letter.letterId)
    }

    private fun setUpAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            adapter = ReAnswersAdapter(letter, answer, binding)
            binding.recyclerview.adapter = adapter
            binding.linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            Log.w("", "ViewModel not initialized when attempting to set up adapter.")
        }
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

    fun obtainViewModel(): ReAnswersViewModel = obtainViewModel(ReAnswersViewModel::class.java)

    fun scrollToPosition(position: Int) {
        if (position >= 0)
            binding.recyclerview.smoothScrollToPosition(position)
    }

}