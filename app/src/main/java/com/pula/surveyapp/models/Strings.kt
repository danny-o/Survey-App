package com.pula.surveyapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName




data class Strings (

	@SerializedName("en")
	@Embedded
	val en : En
)