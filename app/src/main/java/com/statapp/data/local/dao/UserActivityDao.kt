package com.statapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.statapp.data.local.entity.UserActivityEntity
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 활동 데이터 액세스 오브젝트
 */
@Dao
interface UserActivityDao {
    
    /**
     * 특정 사용자의 모든 활동 조회
     */
    @Query("SELECT * FROM user_activities WHERE userId = :userId ORDER BY timestamp DESC")
    fun getActivitiesByUserId(userId: String): Flow<List<UserActivityEntity>>
    
    /**
     * 특정 타입의 활동만 조회
     */
    @Query("SELECT * FROM user_activities WHERE userId = :userId AND type = :type ORDER BY timestamp DESC")
    fun getActivitiesByType(userId: String, type: String): Flow<List<UserActivityEntity>>
    
    /**
     * 활동 기록 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: UserActivityEntity)
    
    /**
     * 여러 활동 기록 한번에 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<UserActivityEntity>)
    
    /**
     * 특정 기간 이전의 오래된 활동 삭제
     */
    @Query("DELETE FROM user_activities WHERE timestamp < :timestamp")
    suspend fun deleteOldActivities(timestamp: Long)
} 