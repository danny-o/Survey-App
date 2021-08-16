package com.pula.surveyapp.local_db

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*
import kotlin.collections.HashMap


class Converters {

    @TypeConverter
    fun jsonObjectToString(value: HashMap<*,*>):String{
        return Gson().toJson(value)

    }
    @TypeConverter
    fun stringToJsonObject(value:String):HashMap<String,String>{
        return Gson().fromJson(value,HashMap::class.java) as HashMap<String,String>
    }
}