package com.pula.surveyapp.network


import com.pula.surveyapp.models.Survey
import retrofit2.http.*

interface SurveyApi {


    @GET("d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getSurvey(): Survey


    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("upload")
    suspend fun uploadSurvey(@Field("survey") json:String)
}