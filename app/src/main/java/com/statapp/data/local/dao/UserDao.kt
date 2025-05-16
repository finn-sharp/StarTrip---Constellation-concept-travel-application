package com.statapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.statapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 데이터 액세스 오브젝트
 */
@Dao
interface UserDao {
    
    /**
     * 사용자 데이터 조회
     */
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: String): Flow<UserEntity?>
    
    /**
     * 사용자 데이터 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    /**
     * 사용자 데이터 업데이트
     */
    @Update
    suspend fun updateUser(user: UserEntity)
    
    /**
     * 모든 사용자 조회
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
} 