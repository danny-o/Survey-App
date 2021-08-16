package com.pula.surveyapp.models

import android.graphics.Path
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.pula.surveyapp.models.Options


@Entity(tableName = "question")
data class Question @JvmOverloads constructor (

	@SerializedName("id")
	@ColumnInfo
	@PrimaryKey
	val id : String,

	@ColumnInfo
	var surveyId:String,

	@SerializedName("question_type")
	@ColumnInfo
	val question_type : String,

	@SerializedName("answer_type")
	@ColumnInfo
	val answer_type : String,

	@SerializedName("question_text")
	@ColumnInfo
	val question_text : String,

	@SerializedName("options")
	@Ignore
	var options : List<Options>?=null,

	@SerializedName("next")
	@ColumnInfo
	val next : String?
)

data class QuestionWithOptions(
	@Embedded
	val question: Question,

	@Relation(
		parentColumn = "id",
		entityColumn = "questionId"
	)
	val options:List<Options>

)