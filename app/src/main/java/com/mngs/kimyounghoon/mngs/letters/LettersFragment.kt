package com.mngs.kimyounghoon.mngs.letters

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentLettersBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupSnackbar

class LettersFragment : AbstractFragment() {
    lateinit var binding: FragmentLettersBinding
    private lateinit var adapter: LettersAdapter

    companion object {
        fun newInstance(): LettersFragment {
            return LettersFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            adapter = LettersAdapter()
            binding.recyclerview.adapter = adapter
        } else {
            Log.w("", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_letters, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
    }

    fun obtainViewModel(): LettersViewModel = obtainViewModel(LettersViewModel::class.java)
}