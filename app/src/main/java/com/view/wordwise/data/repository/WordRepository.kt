package com.view.wordwise.data.repository


import com.view.wordwise.data.api.DictionaryApiService
import com.view.wordwise.data.database.WordDefinitionDao
import com.view.wordwise.data.model.WordDefinitionEntity


import javax.inject.Inject


class WordRepository @Inject constructor(
    private val apiService: DictionaryApiService,
    private val wordDefinitionDao: WordDefinitionDao
) {
    suspend fun getWordDefinition(word: String): Result<WordDefinitionEntity> {
        return try {
            // Try to get the word definition from the cache
            val cachedDefinition = wordDefinitionDao.getWordDefinition(word)
            if (cachedDefinition != null) {
                return Result.success(cachedDefinition)
            }

            // If not found in cache, fetch from the API
            val response = apiService.getWordDefinition(word).first()
            val wordDefinition =
                WordDefinitionEntity(word = response.word, meanings = response.meanings)

            // Save the new word definition to the cache
            wordDefinitionDao.insertWordDefinition(wordDefinition)

            Result.success(wordDefinition)
        } catch (e: Exception) {

                Result.failure(e)

        }
    }
}
