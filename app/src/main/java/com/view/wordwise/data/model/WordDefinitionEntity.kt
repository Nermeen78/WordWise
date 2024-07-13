package com.view.wordwise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_definitions")
data class WordDefinitionEntity(
    @PrimaryKey val word: String,
    val meanings: List<Meaning>
)
