package com.view.wordwise.di

import android.content.Context
import androidx.room.Room
import com.view.wordwise.data.api.DictionaryApiService
import com.view.wordwise.data.database.AppDatabase
import com.view.wordwise.data.database.WordDefinitionDao
import com.view.wordwise.data.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): DictionaryApiService {
        return retrofit.create(DictionaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "wordwise_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDefinitionDao(database: AppDatabase): WordDefinitionDao {
        return database.wordDefinitionDao()
    }
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

}
