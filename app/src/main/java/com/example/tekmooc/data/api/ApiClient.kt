package com.example.tekmooc.data.api

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var CLIENT_ID = "cSzOL3l4M3Yme1eKM3mAjWdeoDcS6XiMr1WKe4Om"
    var TOKEN = "5W4QasX2wKQR7teWCyanpId5Sk0werEwDQHsQndz3jIutkVhjnR73ygJsuhknGPzOHtr7i7yBHR5c3GUxMUPuEif3krjJ3zoUSCVztRrAd0vjUhJCw9o7nX6rz0BIXSt"
    private val BASE_URL = "https://www.udemy.com/api-2.0/"
    private var mRetrofit: Retrofit? = null

    val client: Retrofit
    get() {
        if (mRetrofit == null){
            mRetrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("Authorization", Credentials.basic(
                        CLIENT_ID, TOKEN)).build()
                    chain.proceed(request)
                }.build())
                .build()
        }
        return mRetrofit!!
    }
}