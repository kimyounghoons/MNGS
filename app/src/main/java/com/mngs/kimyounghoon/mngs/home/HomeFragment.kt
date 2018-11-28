package com.mngs.kimyounghoon.mngs.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.mngs.kimyounghoon.mngs.AbstractFragment
import com.mngs.kimyounghoon.mngs.AccountManager
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.databinding.FragmentHomeBinding


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
        setHasOptionsMenu(true)
        showActionBar()
        AccountManager.getInstance().refreshFirebaseToken(context)
        homeFragmentStatePagerAdapter = HomeFragmentStatePagerAdapter(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        actionBarListener?.setTitle(getTitle())
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setUpTabLayout()
        fragmentHomeBinding.viewPager.adapter = homeFragmentStatePagerAdapter
        fragmentHomeBinding.viewPager.offscreenPageLimit = 2
        fragmentHomeBinding.tabLayout.addOnTabSelectedListener(this)
        selectTab(0)

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

    private fun selectTab(position: Int) {
        Handler().post {
            fragmentHomeBinding.tabLayout.getTabAt(position)?.select()
            fragmentHomeBinding.viewPager.currentItem = position
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_logout -> {
            FirebaseAuth.getInstance().signOut()
            locateListener?.openLogin()
            true
        }
        R.id.menu_withdraw -> {
            val user = FirebaseAuth.getInstance().currentUser
            user?.delete()
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            locateListener?.openLogin()
                        } else {
                            //todo 탈퇴 실패
                        }
                    }
            true
        }
        else -> {
            false
        }
    }
}