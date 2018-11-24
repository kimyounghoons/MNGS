package com.mngs.kimyounghoon.mngs

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.support.annotation.VisibleForTesting
import com.google.firebase.iid.FirebaseInstanceId
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.data.User
import com.mngs.kimyounghoon.mngs.data.source.LettersFirebaseDataSource

class AccountManager() {

    var user: User = User(EMPTY, EMPTY)

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AccountManager? = null

        fun getInstance() =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: AccountManager()
                            .also { INSTANCE = it }
                }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    fun refreshFirebaseToken(context: Context?) {
        context?.apply {
            if (needResfreshToken(context)) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    LettersFirebaseDataSource.sendRefreshToken(it.token)
                    setNeedRefereshToken(context)
                }
            }
        }
    }

    private fun setNeedRefereshToken(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(Constants.NEED_REFRESH_TOKEN, false).apply()
    }

    private fun needResfreshToken(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.NEED_REFRESH_TOKEN, true)
}