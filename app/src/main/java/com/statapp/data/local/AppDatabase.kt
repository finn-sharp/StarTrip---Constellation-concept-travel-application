package com.statapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.statapp.data.local.dao.UserActivityDao
import com.statapp.data.local.dao.UserDao
import com.statapp.data.local.entity.UserActivityEntity
import com.statapp.data.local.entity.UserEntity

/**
 * 앱의 메인 데이터베이스
 */
@Database(
    entities = [
        UserEntity::class,
        UserActivityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * 사용자 데이터 DAO
     */
    abstract fun userDao(): UserDao
    
    /**
     * 사용자 활동 DAO
     */
    abstract fun userActivityDao(): UserActivityDao
} 