package com.mngs.kimyounghoon.mngs.sentanswers

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentReceiveAnswerBinding
import com.mngs.kimyounghoon.mngs.receiveanswers.ReceiveAnswersAdapter
import com.mngs.kimyounghoon.mngs.receiveanswers.ReceiveAnswersViewModel
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class ReceiveAnswersFragment : AbstractFragment() {
    override fun getTitle(): String {
        return getString(R.string.receive_answers)
    }

    lateinit var binding: FragmentReceiveAnswerBinding
    private lateinit var adapter: ReceiveAnswersAdapter

    companion object {
        fun newInstance(): ReceiveAnswersFragment {
            return ReceiveAnswersFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        binding.viewModel?.start()
    }

    private fun setUpAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            adapter = ReceiveAnswersAdapter(locateListener!!)
            binding.recyclerview.adapter = adapter
        } else {
            Log.w("", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_receive_answer, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }

    private fun obtainViewModel(): ReceiveAnswersViewModel = obtainViewModel(ReceiveAnswersViewModel::class.java)
}