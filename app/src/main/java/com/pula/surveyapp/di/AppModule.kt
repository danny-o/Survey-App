package com.digitalskies.androidpaging3.di

import android.content.Context
import androidx.room.Room
import com.pula.surveyapp.data.Repository
import com.pula.surveyapp.local_db.SurveyDataBase
import com.pula.surveyapp.network.RestClient
import com.pula.surveyapp.network.SurveyApi
import com.pula.surveyapp.utils.MockInterceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideRoomDatabase(@ApplicationContext application: Context):SurveyDataBase{
        return Room.databaseBuilder(application,SurveyDataBase::class.java,
            SurveyDataBase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRestClient(interceptor: MockInterceptor):Retrofit{
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        okHttpClientBuilder.addInterceptor(interceptor)

        okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS)

        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS)


        return Retrofit.Builder()
            .baseUrl(RestClient.SURVEY_BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesAPi(retrofit: Retrofit):SurveyApi{
        return retrofit.create(SurveyApi::class.java)
    }

    @Provides
    fun providesRepository(@ApplicationContext application: Context,dataBase: SurveyDataBase,surveyApi: SurveyApi):Repository{
        return Repository(application,dataBase,surveyApi)
    }

    @Provides
    fun provideMockInterceptor(): MockInterceptor = MockInterceptor()

}