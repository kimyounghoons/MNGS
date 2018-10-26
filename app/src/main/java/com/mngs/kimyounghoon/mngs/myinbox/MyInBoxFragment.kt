package com.mngs.kimyounghoon.mngs.myinbox

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentMyInBoxBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel

class MyInBoxFragment : Fragment(){
    lateinit var binding: FragmentMyInBoxBinding


    companion object {
        fun newInstance(): MyInBoxFragment {
            return MyInBoxFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_in_box, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        return binding.root
    }

    fun obtainViewModel(): MyInBoxViewModel = obtainViewModel(MyInBoxViewModel::class.java)
}