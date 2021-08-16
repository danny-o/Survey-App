package com.pula.surveyapp.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.work.*
import com.google.gson.Gson
import com.pula.surveyapp.SURVEY_FETCHED
import com.pula.surveyapp.SURVEY_ID
import com.pula.surveyapp.application.dataStore
import com.pula.surveyapp.databinding.ActivityMainBinding
import com.pula.surveyapp.models.Answers
import com.pula.surveyapp.utils.OnNextClickListener
import com.pula.surveyapp.utils.SurveyUploader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),OnNextClickListener {

    lateinit var binding: ActivityMainBinding



    lateinit var mainActivityViewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainActivityViewModel=ViewModelProvider(this).get(MainActivityViewModel::class.java)


        lifecycleScope.launch {

            binding.progressBar.isVisible=true

            val surveyFetched=application.dataStore.data.map { prefs->
                prefs[booleanPreferencesKey(SURVEY_FETCHED)]
            }.first()

            if(surveyFetched!=true){
                mainActivityViewModel.fetchSurvey()
            }

            binding.progressBar.isVisible=false
        }


        mainActivityViewModel.surveyWithQuestions.observe(this,{ surveyWithQuestions->

            if(surveyWithQuestions.isNotEmpty()){


                mainActivityViewModel.startQuestion=surveyWithQuestions[0].survey.start_question_id

                mainActivityViewModel.strings= JSONObject(Gson().toJson(surveyWithQuestions[0].survey.strings.en))

                mainActivityViewModel.answers= Answers(surveyWithQuestions[0].survey.id, HashMap())


                val surveyAdapter=SurveyAdapter(this@MainActivity,this,
                    surveyWithQuestions[0].questions.size.plus(1))





                binding.viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL

                binding.viewPager.isUserInputEnabled=false



                binding.viewPager.adapter=surveyAdapter
            }


        })




    }


    override fun onClickNext() {

        lifecycleScope.launch {
            mainActivityViewModel.insertAnswer()

            //first answers entered, begin upload
            if(binding.viewPager.currentItem==0){

                val surveyID=application.dataStore.data.map { it[stringPreferencesKey(SURVEY_ID)] }.first()
                beginUpload(surveyID.toString())
            }
            binding.viewPager.currentItem+=1
        }

    }

    private fun beginUpload(surveyId:String) {

        Log.d(MainActivity::class.java.simpleName,"surveyId: ${surveyId}")

        val data=Data.Builder().putString(SURVEY_ID,surveyId).build()

        val constraints=Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()


        val periodicWorkRequest=PeriodicWorkRequest.Builder(SurveyUploader::class.java,15,TimeUnit.MINUTES)
                                    .setInputData(data)
                                    .setConstraints(constraints)
                                    .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)



    }


}