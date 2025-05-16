package com.statapp.di

import android.content.Context
import com.statapp.data.repository.GooglePlacesRepository
import com.statapp.domain.repository.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Google Places API 의존성을 제공하는 Hilt 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object PlacesModule {
    
    /**
     * Places 리포지토리 구현체를 제공합니다.
     */
    @Provides
    @Singleton
    fun providePlacesRepository(
        @ApplicationContext context: Context
    ): PlacesRepository {
        return GooglePlacesRepository(context)
    }
} 