package com.example.mvvmretrofit.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val BASE_URL = "https://api.themoviedb.org"
val api_key = "ffdb03735928fcc0efda02a0db29b49e"
val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
val FIRST_PAGE = 1
val POST_PER_PAGE =20

class RetrofitClient {

    companion object {


        fun getClient(): ApiService {
            val requestInterceptor = Interceptor { chain ->
                //interceptor take only argument which is a lambda function  so parenthesis can be omitted
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", api_key)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }
    }
}