package com.statapp.di

import android.content.Context
import androidx.room.Room
import com.statapp.data.local.StarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 애플리케이션 전체에서 사용되는 의존성을 제공하는 Hilt 모듈입니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * 앱 전체에서 사용할 Room 데이터베이스 인스턴스를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideStarDatabase(@ApplicationContext context: Context): StarDatabase {
        return Room.databaseBuilder(
            context,
            StarDatabase::class.java,
            "star_database"
        ).build()
    }
} 