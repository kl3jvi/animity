package com.kl3jvi.animity.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject


@ProvidedTypeConverter
class Converters @Inject constructor(
    private val moshi: Moshi
) {

    @TypeConverter
    fun fromString(value: String): List<GenreModel>? {
        val listType = Types.newParameterizedType(List::class.java, GenreModel::class.java)
        val adapter: JsonAdapter<List<GenreModel>> = moshi.adapter(listType)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromInfoType(type: List<GenreModel>?): String {
        val listType = Types.newParameterizedType(List::class.java, GenreModel::class.java)
        val adapter: JsonAdapter<List<GenreModel>> = moshi.adapter(listType)
        return adapter.toJson(type)
    }
}
