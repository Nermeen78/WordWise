package com.view.wordwise.di

import com.view.wordwise.data.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    @Singleton
    fun provideWordRepository(): WordRepository {
        return Mockito.mock(WordRepository::class.java)
    }

}
