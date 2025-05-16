package com.statapp.di

import android.content.Context
import androidx.room.Room
import com.statapp.data.local.AppDatabase
import com.statapp.data.local.dao.UserActivityDao
import com.statapp.data.local.dao.UserDao
import com.statapp.data.repository.RoomUserRepository
import com.statapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 로컬 데이터베이스 관련 의존성을 제공하는 Hilt 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Room 데이터베이스 인스턴스를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "statapp_database"
        ).build()
    }
    
    /**
     * 사용자 DAO를 제공합니다.
     */
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    
    /**
     * 사용자 활동 DAO를 제공합니다.
     */
    @Provides
    fun provideUserActivityDao(database: AppDatabase): UserActivityDao {
        return database.userActivityDao()
    }
    
    /**
     * 사용자 저장소 구현체를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        userActivityDao: UserActivityDao
    ): UserRepository {
        return RoomUserRepository(userDao, userActivityDao)
    }
} 