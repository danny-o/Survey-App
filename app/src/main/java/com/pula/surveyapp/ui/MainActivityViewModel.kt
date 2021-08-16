package com.pula.surveyapp.ui


import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.*
import com.google.gson.Gson
import com.pula.surveyapp.SURVEY_FETCHED
import com.pula.surveyapp.SURVEY_ID
import com.pula.surveyapp.application.SurveyApplication
import com.pula.surveyapp.application.dataStore
import com.pula.surveyapp.data.Repository
import com.pula.surveyapp.models.*
import com.pula.surveyapp.network.RestClient
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(var surveyApplication: Application,val repository: Repository):AndroidViewModel(surveyApplication) {



    var nextQuestion:String?=null



    val surveyWithQuestions
    get() = repository.getSurveyWithQuestions()

    var strings:JSONObject?=null

    var startQuestion:String?=null

    lateinit var answers:Answers





    suspend fun fetchSurvey(){

        surveyApplication.dataStore.edit { prefs->
            prefs[booleanPreferencesKey(SURVEY_FETCHED)]=true

        }
        repository.fetchSurvey()
    }

     fun getQuestionsWithOptions(id:String): LiveData<List<QuestionWithOptions>> {
        return repository.getQuestionWithOptions(id)
    }

    suspend fun insertAnswer(){

        Log.d(MainActivityViewModel::class.simpleName,Gson().toJson(answers.answerInputs))
        repository.insertAnswer(answers)
    }

    suspend fun getAnswers(id:String): Answers {
        return repository.getAnswers(id)
    }


}