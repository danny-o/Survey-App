package com.pula.surveyapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class Answers(
    @ColumnInfo
    @PrimaryKey
    var id:String,

    @ColumnInfo
    var answerInputs:HashMap<String,String>

)
