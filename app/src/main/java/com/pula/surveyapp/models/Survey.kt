
package com.pula.surveyapp.models


import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.SerializedName




@Entity(tableName = "survey")
data class Survey @JvmOverloads constructor (

	@SerializedName("id")
	@PrimaryKey
	@ColumnInfo
	val id : String,

	@ColumnInfo
	@SerializedName("start_question_id") val start_question_id : String,


	@SerializedName("questions")
	@Ignore
	var questions : List<Question>?=null,


	@SerializedName("strings")
	@Embedded
	val strings : Strings
)
data class SurveyWithQuestions(
		@Embedded
		val survey: Survey,

		@Relation(
				parentColumn = "id",
				entityColumn = "surveyId"
		)
		val questions:List<Question>

)