package com.mngs.kimyounghoon.mngs.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentHomeBinding

class HomeFragment : AbstractFragment() , TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener{

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeFragmentStatePagerAdapter: HomeFragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeFragmentStatePagerAdapter = HomeFragmentStatePagerAdapter(fragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setUpTabLayout()
        return fragmentHomeBinding.root
    }

    private fun setUpTabLayout() {
        for (i in 0 until homeFragmentStatePagerAdapter.count) {
            val tab = fragmentHomeBinding.tabLayout.newTab()
            tab.setCustomView(homeFragmentStatePagerAdapter.getCustomView())
            homeFragmentStatePagerAdapter.bindCustomView(tab.getCustomView(), i)
            tabLayout.addTab(tab)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageSelected(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}