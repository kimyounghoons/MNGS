package com.mngs.kimyounghoon.mngs.Retrofits.interfaces


import com.google.gson.JsonObject
import com.mngs.kimyounghoon.mngs.models.FirebasePushData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface FirebasePushService {

    @POST("fcm/send")
    fun pushAnswer(@HeaderMap headers : HashMap<String,Any> , @Body body: FirebasePushData) : Call<JsonObject>

}