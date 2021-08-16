package com.pula.surveyapp.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.pula.surveyapp.SURVEY_ID
import com.pula.surveyapp.application.dataStore
import com.pula.surveyapp.data.Repository
import com.pula.surveyapp.network.SurveyApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class SurveyUploader (val context:Context,params:WorkerParameters):CoroutineWorker(context,params){





    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SurveyUploaderEntryPoint{

        fun repository():Repository

        fun surveyAPI():SurveyApi

    }


    override suspend fun doWork(): Result {



        val surveyId=inputData.getString(SURVEY_ID)



        var hiltEntryPoints=EntryPointAccessors.fromApplication(context.applicationContext,SurveyUploaderEntryPoint::class.java)



        val repository=hiltEntryPoints.repository()


        val answers=repository.getAnswers(surveyId.toString())

        hiltEntryPoints=EntryPointAccessors.fromApplication(context.applicationContext,SurveyUploaderEntryPoint::class.java)

        hiltEntryPoints.surveyAPI().uploadSurvey(Gson().toJson(answers))

        Log.d("worker","${answers.answerInputs} and $surveyId")

        return Result.retry()

    }
}