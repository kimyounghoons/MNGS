package com.mngs.kimyounghoon.mngs.writeletter

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentWriteLetterBinding
import com.mngs.kimyounghoon.mngs.home.HomeFragment
import com.mngs.kimyounghoon.mngs.utils.obtainViewModel
import com.mngs.kimyounghoon.mngs.utils.setupToast

class WriteLetterFragment : AbstractFragment(), WriteLetterNavigator {
    override fun getTitle(): String {
        return getString(R.string.write_letter)
    }

    override fun onLetterSended() {

    }

    lateinit var binding: FragmentWriteLetterBinding

    companion object {
        fun newInstance(): WriteLetterFragment {
            return WriteLetterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_letter, container, false)
        binding.apply {
            viewModel = obtainViewModel()
        }

        obtainViewModel().let {
            it.sendLetterCommand.observe(this, Observer {
                Toast.makeText(context, "StartToSendLetter", Toast.LENGTH_SHORT).show()
            })
            it.completed.observe(this, Observer {
                if(parentFragment is HomeFragment){
                    (parentFragment as HomeFragment).selectTab(0)
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainViewModel().let {
            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }

    private fun obtainViewModel(): WriteLetterViewModel = obtainViewModel(WriteLetterViewModel::class.java)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_letter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_write_letter -> {
            obtainViewModel().sendLetter()
            true
        }
        else -> {
            false
        }
    }

}