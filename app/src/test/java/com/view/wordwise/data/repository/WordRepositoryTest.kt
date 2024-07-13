package com.view.wordwise.data.repository


import com.view.wordwise.data.api.DictionaryApiService
import com.view.wordwise.data.database.WordDefinitionDao
import com.view.wordwise.data.model.Definition
import com.view.wordwise.data.model.Meaning
import com.view.wordwise.data.model.Phonetic
import com.view.wordwise.data.model.WordDefinitionEntity
import com.view.wordwise.data.model.WordDefinitionModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.Exception

@ExperimentalCoroutinesApi
class WordRepositoryTest {

    // Mock dependencies
    @Mock
    lateinit var apiService: DictionaryApiService

    @Mock
    lateinit var wordDefinitionDao: WordDefinitionDao

    // System under test
    lateinit var repository: WordRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WordRepository(apiService, wordDefinitionDao)
    }

    @Test
    fun `test getWordDefinition from cache`() = runBlocking {
        val word = "example"
        val definition = Definition(
            listOf(), "an example definition", null, listOf()
        )
        val cachedDefinition =  WordDefinitionEntity(
            word, listOf(
                Meaning(partOfSpeech = "noun", definitions = listOf(definition))
            )
        )
        `when`(wordDefinitionDao.getWordDefinition(word)).thenReturn(cachedDefinition)

        val result = repository.getWordDefinition(word)

        assertEquals(Result.success(cachedDefinition), result)
    }

    @Test
    fun `test getWordDefinition from API`() = runBlocking {
        val word = "example"
        val definition = Definition(
            listOf(), "an example definition", null, listOf()
        )
        val apiResponse =  WordDefinitionModel(
            meanings = listOf(
                Meaning(partOfSpeech = "noun", definitions = listOf(definition))
            ), origin = "", phonetic = "", phonetics = listOf(Phonetic("","")),
            word = word
        )
        `when`(wordDefinitionDao.getWordDefinition(word)).thenReturn(null)
        `when`(apiService.getWordDefinition(word)).thenReturn(listOf(apiResponse))

        val result = repository.getWordDefinition(word)
        val wordDefinitionEntity=WordDefinitionEntity(apiResponse.word,apiResponse.meanings)
        assertEquals(Result.success(wordDefinitionEntity), result)
    }

    @Test
    fun `test getWordDefinition exception handling`() = runBlocking {
        val word = "example"
        val exception = Exception("API error")
        `when`(wordDefinitionDao.getWordDefinition(word)).thenReturn(null)
        `when`(apiService.getWordDefinition(word)).thenThrow(exception)

        val result = repository.getWordDefinition(word)

        assertEquals(Result.failure<WordDefinitionEntity>(exception), result)
    }
}
