package com.statapp.domain.model

/**
 * 사용자 활동 모델
 * 로그인, 검색 등 사용자의 활동을 기록하는 데 사용됩니다.
 */
data class UserActivity(
    val id: String = "",
    val userId: String = "",
    val type: String = "",  // login, search, view 등 활동 유형
    val timestamp: Long = System.currentTimeMillis(),
    val data: Map<String, Any> = mapOf()  // 활동 관련 추가 데이터
) 