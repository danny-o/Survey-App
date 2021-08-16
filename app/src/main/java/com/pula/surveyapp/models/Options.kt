package com.pula.surveyapp.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "options")
data class Options(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var Id:Int,

    @ColumnInfo
    var questionId:String?,


    @SerializedName("value")
    @ColumnInfo
    val value:String,

    @ColumnInfo
    @SerializedName("display_text")
    val displayText:String
)
