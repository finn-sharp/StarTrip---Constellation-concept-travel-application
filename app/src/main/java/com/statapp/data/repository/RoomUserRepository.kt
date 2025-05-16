package com.statapp.data.repository

import android.util.Log
import com.statapp.data.local.dao.UserActivityDao
import com.statapp.data.local.dao.UserDao
import com.statapp.data.local.entity.UserActivityEntity
import com.statapp.data.local.entity.UserEntity
import com.statapp.domain.model.UserActivity
import com.statapp.domain.model.UserData
import com.statapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Room 데이터베이스를 사용한 사용자 데이터 저장소 구현
 */
class RoomUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userActivityDao: UserActivityDao
) : UserRepository {

    private val TAG = "RoomUserRepository"

    override fun getUserData(userId: String): Flow<UserData?> {
        Log.d(TAG, "사용자 데이터 구독 시작: $userId")
        return userDao.getUserById(userId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun updateUserData(userData: UserData): Boolean {
        return try {
            Log.d(TAG, "사용자 데이터 업데이트: ${userData.userId}")
            val userEntity = UserEntity.fromDomain(userData)
            userDao.insertUser(userEntity)
            true
        } catch (e: Exception) {
            Log.e(TAG, "사용자 데이터 업데이트 실패: ${e.message}")
            false
        }
    }

    override suspend fun saveUserActivity(userId: String, activity: UserActivity): Boolean {
        return try {
            Log.d(TAG, "사용자 활동 저장: $userId, 타입: ${activity.type}")
            val activityWithUserId = if (activity.userId.isEmpty()) {
                activity.copy(userId = userId)
            } else {
                activity
            }
            
            val activityEntity = UserActivityEntity.fromDomain(activityWithUserId)
            userActivityDao.insertActivity(activityEntity)
            
            // 오래된 활동 기록 정리 (선택적)
            val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
            userActivityDao.deleteOldActivities(thirtyDaysAgo)
            
            true
        } catch (e: Exception) {
            Log.e(TAG, "사용자 활동 저장 실패: ${e.message}")
            false
        }
    }
} 