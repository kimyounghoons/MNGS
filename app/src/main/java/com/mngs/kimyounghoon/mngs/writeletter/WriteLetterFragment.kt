package com.mngs.kimyounghoon.mngs.writeletter

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentWriteLetterBinding
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupSnackbar

class WriteLetterFragment : AbstractFragment() , WriteLetterNavigator {
    override fun onLetterSended() {

    }

    lateinit var binding: FragmentWriteLetterBinding

    companion object {
        fun newInstance(): WriteLetterFragment {
            return WriteLetterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_letter, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }
        binding.sendButton.setOnClickListener {
            binding.viewModel?.sendLetter()
        }

        obtainViewModel().let{
            it.sendLetterCommand.observe(this, Observer {
                Toast.makeText(context,"StartToSendLetter",Toast.LENGTH_LONG).show()
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
    }

    fun obtainViewModel(): WriteLetterViewModel = obtainViewModel(WriteLetterViewModel::class.java)

}