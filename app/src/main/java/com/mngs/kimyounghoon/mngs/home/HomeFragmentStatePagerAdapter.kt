package com.mngs.kimyounghoon.mngs.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.letters.LettersFragment
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterFragment

class HomeFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    lateinit var fragments: ArrayList<Fragment>

    interface Type {
        companion object {
            val LETTERS: Int = 1
            val WRITE: Int = 2
        }
    }

    init {
        fragments.add(WriteLetterFragment.newInstance())
        fragments.add(LettersFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            Type.LETTERS -> "편지들"
            Type.WRITE -> "편지쓰기"
            else -> {
                ""
            }
        }
    }

    fun getCustomView(): Int {
       return R.layout.item_main_tab
    }

    fun bindCustomView(customView: View, position: Int){

    }
}