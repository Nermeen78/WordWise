package com.view.wordwise.data.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.view.wordwise.data.model.Meaning

class Converters {
    @TypeConverter
    fun fromDefinition(meanings: List<Meaning>): String {
        return Gson().toJson(meanings)
    }

    @TypeConverter
    fun toDefinition(definitionString: String): List<Meaning> {
        val type = object : TypeToken<List<Meaning>>() {}.type
        return Gson().fromJson(definitionString, type)
    }
}
