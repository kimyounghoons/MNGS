package com.mngs.kimyounghoon.mngs.firebases

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.mngs.kimyounghoon.mngs.Retrofits.RetrofitHelper
import com.mngs.kimyounghoon.mngs.Retrofits.interfaces.FirebasePushService
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.SERVER_KEY
import com.mngs.kimyounghoon.mngs.models.FirebasePushData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebasePushDAO(val context: Context) {

    fun postPushAnswer(body: FirebasePushData) {
        val headers = HashMap<String, Any>()
        headers["Authorization"] = "key=$SERVER_KEY"
        headers["Content-Type"] = "application/json"

        val call: Call<JsonObject> = RetrofitHelper.getRetrofit().create(FirebasePushService::class.java).pushAnswer(headers,body)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.d("푸시", "fail")
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.d("푸시", "성공")
                } else {
                    Log.d("푸시", "onResponse 실패")
                }
            }

        })
    }

}