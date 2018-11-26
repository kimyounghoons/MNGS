package com.mngs.kimyounghoon.mngs.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mngs.kimyounghoon.mngs.MngsApp
import com.mngs.kimyounghoon.mngs.R
import com.mngs.kimyounghoon.mngs.letters.LettersFragment
import com.mngs.kimyounghoon.mngs.myinbox.MyInBoxFragment
import com.mngs.kimyounghoon.mngs.sentanswers.ReceiveAnswersFragment
import com.mngs.kimyounghoon.mngs.sentanswers.SentAnswersFragment
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterFragment

class HomeFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    var fragments: ArrayList<Fragment> = ArrayList()

    interface Type {
        companion object {
            const val LETTERS: Int = 0
            const val RECEIVE_ANSWER : Int = 1
            const val SENT_ANSWER: Int = 2
            const val MYINBOX: Int = 3
        }
    }

    init {
        fragments.add(LettersFragment.newInstance())
        fragments.add(ReceiveAnswersFragment.newInstance())
        fragments.add(SentAnswersFragment.newInstance())
        fragments.add(MyInBoxFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            Type.LETTERS -> MngsApp.getAppContext().getString(R.string.mail_box)
            Type.RECEIVE_ANSWER ->  MngsApp.getAppContext().getString(R.string.receive_answers)
            Type.SENT_ANSWER -> MngsApp.getAppContext().getString(R.string.sent_answers)
            else -> {
                MngsApp.getAppContext().getString(R.string.sent_mail)
            }
        }
    }

    fun getPageTitleImage(position: Int): Int {
        return when (position) {
            Type.LETTERS -> R.drawable.mailbox
            Type.RECEIVE_ANSWER -> R.drawable.sent_answer
            Type.SENT_ANSWER -> R.drawable.sent_answer
            else -> {
                R.drawable.letters
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