package com.kl3jvi.animity.persistence

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import java.lang.reflect.Type
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor() {

    /**
     * It converts a JSON string to a list of GenreModel objects.
     *
     * @param value The JSON string to be converted to a list of objects.
     * @return A list of GenreModel objects
     */
    @TypeConverter
    fun toInfoType(value: String): List<GenreModel>? {
        val listType: Type = object : TypeToken<ArrayList<GenreModel?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * It converts a list of GenreModel objects to a JSON string.
     *
     * @param list List<GenreModel>?
     * @return A string of the list of GenreModel objects
     */
    @TypeConverter
    fun fromInfoType(list: List<GenreModel>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
