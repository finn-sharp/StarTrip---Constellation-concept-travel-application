package com.statapp.domain.model

/**
 * 사용자 데이터 모델
 * Firestore에 저장되는 사용자 정보를 표현합니다.
 */
data class UserData(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis(),
    val preferences: Map<String, Any> = mapOf()
) 