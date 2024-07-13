package com.view.wordwise.data.api

import com.view.wordwise.data.model.WordDefinitionModel
import retrofit2.http.GET
import retrofit2.http.Path


interface DictionaryApiService {
    @GET("/api/v2/entries/en/{word}")
    suspend fun getWordDefinition(@Path("word") word: String): List<WordDefinitionModel>
}
