package com.mngs.kimyounghoon.mngs

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mngs.kimyounghoon.mngs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() ,LocateListener{

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        openSplash()
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
        supportFragmentManager.beginTransaction().add(R.id.container, SplashFragment.newInstance()).commit()
    }

}
