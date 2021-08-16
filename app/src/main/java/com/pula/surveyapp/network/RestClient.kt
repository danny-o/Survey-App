package com.pula.surveyapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    const val SURVEY_BASE_URL="https://run.mocky.io/v3/"
    fun  getSurveyApi():SurveyApi{
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS)

        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS)


        return Retrofit.Builder()
            .baseUrl(SURVEY_BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SurveyApi::class.java)
    }
}