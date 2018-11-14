package com.mngs.kimyounghoon.mngs.sentanswers

import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R

class SentAnswersFragment : AbstractFragment() {
    override fun getTitle(): String {
        return getString(R.string.sent_answers)
    }

    //todo

//    lateinit var binding: FragmentMyInBoxBinding
//    private lateinit var adapter: MyInBoxAdapter
//
//    companion object {
//        fun newInstance(): MyInBoxFragment {
//            return MyInBoxFragment()
//        }
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        setUpAdapter()
//        binding.viewModel?.start()
//    }
//
//    private fun setUpAdapter() {
//        val viewModel = binding.viewModel
//        if (viewModel != null) {
//            adapter = MyInBoxAdapter(locateListener)
//            binding.recyclerview.adapter = adapter
//        } else {
//            Log.w("", "ViewModel not initialized when attempting to set up adapter.")
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_in_box, container, false)
//        binding.apply {
//            viewModel = obtainViewModel()
//        }
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        obtainViewModel().let {
//            view.setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
//        }
//    }
//
//    private fun obtainViewModel(): MyInBoxViewModel = obtainViewModel(MyInBoxViewModel::class.java)
}