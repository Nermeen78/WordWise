package com.view.wordwise.data.database



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.view.wordwise.data.database.util.Converters
import com.view.wordwise.data.model.WordDefinitionEntity

@Database(entities = [WordDefinitionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDefinitionDao(): WordDefinitionDao
}
