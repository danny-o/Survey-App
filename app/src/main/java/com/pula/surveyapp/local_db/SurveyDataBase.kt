package com.pula.surveyapp.local_db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pula.surveyapp.models.Answers
import com.pula.surveyapp.models.Options
import com.pula.surveyapp.models.Question
import com.pula.surveyapp.models.Survey

@Database(
    entities = [Survey::class, Question::class,Options::class,Answers::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SurveyDataBase:RoomDatabase() {

    abstract fun surveyDao():SurveyDao

    companion object{
        const val DATABASE_NAME="survey_database"

        /*@Volatile
        private var INSTANCE:SurveyDataBase?=null

        fun getInstance(context: Context):SurveyDataBase=

            INSTANCE?: synchronized(this){

                INSTANCE?: buildDatabase(context)
                    .also { INSTANCE=it }
            }

        private fun buildDatabase(context: Context)=
            Room.databaseBuilder(
                context,
                SurveyDataBase::class.java,
                DATABASE_NAME
            )
                .build()*/

    }

}