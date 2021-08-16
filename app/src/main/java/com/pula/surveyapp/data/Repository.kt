package com.pula.surveyapp.data


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import com.pula.surveyapp.SURVEY_ID
import com.pula.surveyapp.application.dataStore
import com.pula.surveyapp.local_db.SurveyDataBase
import com.pula.surveyapp.models.*
import com.pula.surveyapp.network.RestClient
import com.pula.surveyapp.network.SurveyApi
import com.pula.surveyapp.ui.MainActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class Repository @Inject constructor(val application: Context,var dataBase: SurveyDataBase,var surveyApi: SurveyApi) {





    suspend fun fetchSurvey(){

        val survey= surveyApi.getSurvey()

        Log.d(Repository::class.java.simpleName,"surveyId: ${survey.id}")

        application.dataStore.edit { it[stringPreferencesKey(SURVEY_ID)]=survey.id }

        dataBase.surveyDao().insertAllEntities(survey)



    }



    fun getSurveyWithQuestions(): LiveData<List<SurveyWithQuestions>> {
        return dataBase.surveyDao().getSurveyWithQuestions()
    }

     fun getQuestionWithOptions(id:String): LiveData<List<QuestionWithOptions>> {
            return dataBase.surveyDao().getQuestionWithOptions()
    }

    suspend fun insertAnswer(answers: Answers){
        dataBase.surveyDao().insertAnswer(answers)
    }

    suspend fun getAnswers(id:String): Answers {
        return dataBase.surveyDao().getAnswers(id)
    }

}