package com.pula.surveyapp.local_db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.pula.surveyapp.models.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Dao
abstract  class SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSurvey(survey:Survey)


    @Transaction
    @Query("SELECT * from survey")
    abstract fun getSurveyWithQuestions():LiveData<List<SurveyWithQuestions>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertQuestions(questions:List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOptions(options:List<Options>)

    @Insert
    @Transaction
    suspend fun insertAllEntities(survey: Survey){


        insertSurvey(survey)



        survey.questions?.let {questions->
            questions.forEach {
                it.surveyId=survey.id

                Log.d("Dao", survey.id)
            }

            insertQuestions(questions)
        }






        survey.questions?.forEach{question ->

            question.options?.forEach{option->
                option.questionId=question.id


            }

            question.options?.let {
                Log.d("DAo", it.size.toString())
                insertOptions(it)
            }


        }
    }

    @Transaction
    @Query("SELECT * from question")
    abstract fun getQuestionWithOptions():LiveData<List<QuestionWithOptions>>

    @Query("SELECT * from question")
    abstract fun getQuestions():LiveData<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnswer(answers: Answers)

    @Query("SELECT *from answers where id=:id")
    abstract suspend fun getAnswers(id:String):Answers

}