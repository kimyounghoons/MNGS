package com.mngs.kimyounghoon.mngs.Retrofits

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.FIREBASE_PUSH_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private var retrofit = Retrofit.Builder().baseUrl(FIREBASE_PUSH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(getOkHttpClient())
            .build()

    fun getRetrofit(): Retrofit {
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        return builder.build()
    }
}