package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MngsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getAppContext(): Context {
            return this.context
        }
    }
}