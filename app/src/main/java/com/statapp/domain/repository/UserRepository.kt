package com.statapp.domain.repository

import com.statapp.domain.model.UserData
import com.statapp.domain.model.UserActivity
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 데이터 관련 리포지토리 인터페이스
 * 사용자 정보와 활동을 저장하고 조회하는 기능을 제공합니다.
 */
interface UserRepository {
    /**
     * 사용자 데이터를 Flow로 구독합니다.
     * @param userId 사용자 ID
     * @return 실시간으로 업데이트되는 사용자 데이터
     */
    fun getUserData(userId: String): Flow<UserData?>

    /**
     * 사용자 데이터를 업데이트합니다.
     * @param userData 업데이트할 사용자 데이터
     * @return 성공 여부
     */
    suspend fun updateUserData(userData: UserData): Boolean

    /**
     * 사용자 활동을 저장합니다.
     * @param userId 사용자 ID
     * @param activity 기록할 활동 정보
     * @return 성공 여부
     */
    suspend fun saveUserActivity(userId: String, activity: UserActivity): Boolean
} 