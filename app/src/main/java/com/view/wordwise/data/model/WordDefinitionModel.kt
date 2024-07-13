package com.view.wordwise.data.model

data class WordDefinitionModel(
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<Phonetic>,
    val word: String
)