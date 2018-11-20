package com.mngs.kimyounghoon.mngs.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.iid.FirebaseInstanceId
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentHomeBinding
import com.mngs.kimyounghoon.mngs.firebases.FirebasePushDAO
import com.mngs.kimyounghoon.mngs.models.FirebasePushData


class HomeFragment : AbstractFragment(), TabLayout.OnTabSelectedListener {
    override fun getTitle(): String {
        return getString(R.string.app_name)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeFragmentStatePagerAdapter: HomeFragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeFragmentStatePagerAdapter = HomeFragmentStatePagerAdapter(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        actionBarListener?.setTitle(getTitle())
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setUpTabLayout()
        fragmentHomeBinding.viewPager.adapter = homeFragmentStatePagerAdapter
        fragmentHomeBinding.viewPager.offscreenPageLimit = 2
        fragmentHomeBinding.tabLayout.addOnTabSelectedListener(this)

        fragmentHomeBinding.fabWriteLetter.setOnClickListener {
            locateListener?.openWriteLetter()
        }
        return fragmentHomeBinding.root
    }

    private fun setUpTabLayout() {
        for (position in 0 until homeFragmentStatePagerAdapter.count) {
            val tab = fragmentHomeBinding.tabLayout.newTab()
            tab.setCustomView(homeFragmentStatePagerAdapter.getCustomView())
            homeFragmentStatePagerAdapter.bindCustomView(tab.customView!!, position)
            fragmentHomeBinding.tabLayout.addTab(tab)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.apply {
            fragmentHomeBinding.tabLayout.getTabAt(tab.position)?.select()
            fragmentHomeBinding.viewPager.currentItem = tab.position
        }
    }

    fun selectTab(position: Int) {
        fragmentHomeBinding.tabLayout.getTabAt(position)?.select()
    }
}