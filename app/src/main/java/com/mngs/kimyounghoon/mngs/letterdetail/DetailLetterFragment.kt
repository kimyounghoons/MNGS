package com.mngs.kimyounghoon.mngs.letterdetail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.databinding.FragmentDetailLetterBinding

class DetailLetterFragment : AbstractFragment() {
    lateinit var title: String
    lateinit var content: String
    lateinit var binding: FragmentDetailLetterBinding

    companion object {
        private const val KEY_LETTER_DETAIL_TITLE = "KEY_LETTER_DETAIL_TITLE"
        private const val KEY_LETTER_DETAIL_CONTENT = "KEY_LETTER_DETAIL_CONTENT"

        fun newInstance(title: String, content: String): DetailLetterFragment {
            val letterDetailFragment = DetailLetterFragment()
            val bundle = Bundle()
            bundle.putString(KEY_LETTER_DETAIL_TITLE, title)
            bundle.putString(KEY_LETTER_DETAIL_CONTENT, content)

            letterDetailFragment.arguments = bundle
            return DetailLetterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(KEY_LETTER_DETAIL_TITLE) ?: Constants.EMPTY
        content = arguments?.getString(KEY_LETTER_DETAIL_CONTENT) ?: Constants.EMPTY
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_letter, container, false)
        binding.apply {
            viewModel = DetailLetterViewModel()
        }
        binding.title.text = title
        binding.content.text = content

        return binding.root
    }
}