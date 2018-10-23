package com.mngs.kimyounghoon.mngs.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.letters.LettersFragment
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterFragment

class HomeFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    var fragments: ArrayList<Fragment> = ArrayList()

    interface Type {
        companion object {
            const val LETTERS: Int = 1
            const val WRITE: Int = 2
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

    fun getPageTitleImage(position: Int): Int {
        return when (position) {
            Type.LETTERS -> R.drawable.letters
            Type.WRITE -> R.drawable.write_letter
            else -> {
                R.drawable.write_letter
            }
        }
    }

    fun getCustomView(): Int {
        return R.layout.item_main_tab
    }

    fun bindCustomView(customView: View, position: Int) {
        val textView = customView.findViewById<TextView>(R.id.title)
        textView.text = getPageTitle(position)
        val image = customView.findViewById<ImageView>(R.id.image)
        image.setImageResource(getPageTitleImage(position))
    }
}