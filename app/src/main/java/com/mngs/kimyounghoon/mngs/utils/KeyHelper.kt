package com.mngs.kimyounghoon.mngs.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class KeyHelper{
    companion object {
        fun getHashKey(context : Context){
            try {
                val info = context.packageManager.getPackageInfo(
                        "com.mngs.kimyounghoon.mngs",
                        PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.e("HashKey:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("log", "log")
            } catch (e: NoSuchAlgorithmException) {
                Log.e("log", "log")
            }
        }
    }

}