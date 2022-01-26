package com.kl3jvi.animity.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import java.lang.reflect.Type
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor() {

    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<GenreModel?>? {
        val gson = Gson()
        if (data == null) {
            return emptyList()
        }
        val listType: Type = object : TypeToken<List<GenreModel?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<GenreModel?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }
}
