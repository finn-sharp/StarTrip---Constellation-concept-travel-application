package com.statapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room 데이터베이스 클래스입니다.
 * 모든 DAO와 Entity를 포함합니다.
 */
@Database(
    entities = [
        StarEntity::class, 
        ConstellationEntity::class, 
        ConstellationStarCrossRef::class, 
        StarConnectionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BusinessHoursConverter::class)
abstract class StarDatabase : RoomDatabase() {
    /**
     * Star DAO를 반환합니다.
     */
    abstract fun starDao(): StarDao
} 