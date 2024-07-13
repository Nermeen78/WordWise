package com.view.wordwise.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.wordwise.data.model.WordDefinitionEntity

@Dao
interface WordDefinitionDao {
    @Query("SELECT * FROM word_definitions WHERE word = :word")
    suspend fun getWordDefinition(word: String): WordDefinitionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordDefinition(wordDefinition: WordDefinitionEntity)
}
