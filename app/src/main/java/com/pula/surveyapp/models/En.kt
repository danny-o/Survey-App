
package com.pula.surveyapp.models
import com.google.gson.annotations.SerializedName




data class En (

	@SerializedName("q_farmer_name") val q_farmer_name : String,
	@SerializedName("q_farmer_gender") val q_farmer_gender : String,
	@SerializedName("opt_male") val opt_male : String,
	@SerializedName("opt_female") val opt_female : String,
	@SerializedName("opt_other") val opt_other : String,
	@SerializedName("q_size_of_farm") val q_size_of_farm : String
)