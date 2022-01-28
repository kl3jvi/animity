package com.kl3jvi.animity.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import java.lang.reflect.Type
import javax.inject.Inject


@ProvidedTypeConverter
class Converters @Inject constructor() {

    @TypeConverter
    fun toInfoType(value: String): List<GenreModel>? {
        val listType: Type = object : TypeToken<ArrayList<GenreModel?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromInfoType(list: List<GenreModel>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
