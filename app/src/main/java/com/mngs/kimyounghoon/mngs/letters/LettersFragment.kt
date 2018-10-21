package com.mngs.kimyounghoon.mngs.letters

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentLettersBinding

class LettersFragment : AbstractFragment() {
    lateinit var binding: FragmentLettersBinding

    companion object {
        fun newInstance(): LettersFragment {
            return LettersFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_letters, container, false)
        
        return binding.root
    }
}