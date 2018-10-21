package com.mngs.kimyounghoon.mngs.writeletter

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentWriteLetterBinding

class WriteLetterFragment : AbstractFragment() {
    lateinit var binding: FragmentWriteLetterBinding

    companion object {
        fun newInstance(): WriteLetterFragment {
            return WriteLetterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_letter, container, false)
        return binding.root
    }
}