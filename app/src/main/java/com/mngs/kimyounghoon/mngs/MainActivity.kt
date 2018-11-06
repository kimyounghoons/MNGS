package com.mngs.kimyounghoon.mngs

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.google.gson.GsonBuilder
import com.mngs.kimyounghoon.mngs.answerletter.AnswerFragment
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ActivityMainBinding
import com.mngs.kimyounghoon.mngs.home.HomeFragment
import com.mngs.kimyounghoon.mngs.letterdetail.DetailLetterFragment
import com.mngs.kimyounghoon.mngs.login.LoginFragment
import com.mngs.kimyounghoon.mngs.signup.SignupFragment


class MainActivity : AppCompatActivity(), LocateListener, ActionBarListener {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        openSplash()
    }

    override fun hide() {
        supportActionBar?.hide()
    }

    override fun show() {
        supportActionBar?.show()
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun openLogin() {
        supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit()
    }

    override fun openSignup() {
        supportFragmentManager.beginTransaction().replace(R.id.container, SignupFragment.newInstance()).commit()
    }

    override fun openHome() {
        supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment.newInstance()).commit()
    }

    override fun openSplash() {
        supportFragmentManager.beginTransaction().replace(R.id.container, SplashFragment.newInstance()).commit()
    }

    override fun openLetterDetail(letter: Letter) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLetter = gson.toJson(letter)
        supportFragmentManager.beginTransaction().replace(R.id.container, DetailLetterFragment.newInstance(jsonLetter)).addToBackStack(null).commit()
    }

    override fun openAnswer(letter: Letter) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLetter = gson.toJson(letter)
        supportFragmentManager.beginTransaction().replace(R.id.container, AnswerFragment.newInstance(jsonLetter)).addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
