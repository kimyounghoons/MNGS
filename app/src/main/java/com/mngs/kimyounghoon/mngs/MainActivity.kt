package com.mngs.kimyounghoon.mngs

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mngs.kimyounghoon.mngs.answerletter.AnswerFragment
import com.mngs.kimyounghoon.mngs.answers.AnswersFragment
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_LETTER
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.databinding.ActivityMainBinding
import com.mngs.kimyounghoon.mngs.home.HomeFragment
import com.mngs.kimyounghoon.mngs.letterdetail.DetailLetterFragment
import com.mngs.kimyounghoon.mngs.login.LoginFragment
import com.mngs.kimyounghoon.mngs.reanswer.ReAnswerFragment
import com.mngs.kimyounghoon.mngs.reanswers.ReAnswersFragment
import com.mngs.kimyounghoon.mngs.signup.SignupFragment
import com.mngs.kimyounghoon.mngs.splash.SplashFragment
import com.mngs.kimyounghoon.mngs.writeletter.WriteLetterFragment


class MainActivity : AppCompatActivity(), LocateListener, ActionBarListener {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (intent.extras.getString(JSON_ANSWER) != null) {
            val jsonLetter = intent.extras.getString(JSON_LETTER)
            val jsonAnswer = intent.extras.getString(JSON_ANSWER, EMPTY)
            if (supportFragmentManager.backStackEntryCount > 0) {
                if (jsonLetter == null) {
                    val answer = Gson().fromJson(jsonAnswer, Answer::class.java)
                    openReAnswers(answer)
                } else {
                    openReAnswers(jsonLetter, jsonAnswer)
                }
                return
            } else {
                openHome()
                if (jsonLetter == null) {
                    val answer = Gson().fromJson(jsonAnswer, Answer::class.java)
                    openReAnswers(answer)
                } else {
                    openReAnswers(jsonLetter, jsonAnswer)
                }
                return
            }
        }
        openSplash()
    }

    fun showQuickGuide() {
        supportActionBar?.hide()
        activityMainBinding.guideContainer.visibility = View.VISIBLE
        activityMainBinding.closeGuideButton.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.need_guide), false).apply()
            activityMainBinding.guideContainer.visibility = View.GONE
            supportActionBar?.show()
        }
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

    override fun openWriteLetter() {
        supportFragmentManager.beginTransaction().replace(R.id.container, WriteLetterFragment.newInstance()).addToBackStack(null).commit()
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

    override fun openAnswers(letter: Letter) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLetter = gson.toJson(letter)
        supportFragmentManager.beginTransaction().replace(R.id.container, AnswersFragment.newInstance(jsonLetter)).addToBackStack(null).commit()

    }

    override fun openReAnswer(answer: Answer) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonAnswer = gson.toJson(answer)
        supportFragmentManager.beginTransaction().replace(R.id.container, ReAnswerFragment.newInstance(jsonAnswer)).addToBackStack(null).commit()
    }

    override fun openReAnswers(letter: Letter, answer: Answer) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLetter = gson.toJson(letter)
        val jsonAnswer = gson.toJson(answer)
        supportFragmentManager.beginTransaction().replace(R.id.container, ReAnswersFragment.newInstance(jsonLetter, jsonAnswer)).addToBackStack(null).commit()
    }

    fun openReAnswers(jsonLetter: String, jsonAnswer: String) {
        supportFragmentManager.beginTransaction().replace(R.id.container, ReAnswersFragment.newInstance(jsonLetter, jsonAnswer)).addToBackStack(null).commit()
    }

    override fun openReAnswers(answer: Answer) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonAnswer = gson.toJson(answer)

        supportFragmentManager.beginTransaction().replace(R.id.container, ReAnswersFragment.newInstance(jsonAnswer)).addToBackStack(null).commit()

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.apply {
            if (intent.extras.getString(JSON_ANSWER) != null) {
                val jsonLetter = intent.extras.getString(JSON_LETTER)
                val jsonAnswer = intent.extras.getString(JSON_ANSWER, EMPTY)
                if (supportFragmentManager.backStackEntryCount > 0) {
                    if (jsonLetter == null) {
                        val answer = Gson().fromJson(jsonAnswer, Answer::class.java)
                        openReAnswers(answer)
                    } else {
                        openReAnswers(jsonLetter, jsonAnswer)
                    }
                    return
                } else {
                    openHome()
                    if (jsonLetter == null) {
                        val answer = Gson().fromJson(jsonAnswer, Answer::class.java)
                        openReAnswers(answer)
                    } else {
                        openReAnswers(jsonLetter, jsonAnswer)
                    }
                    return
                }
            }
        }
    }

}
