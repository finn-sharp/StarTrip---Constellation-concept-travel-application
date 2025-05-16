package com.statapp.domain.model

/**
 * 사용자 도메인 모델
 *
 * @property uid 사용자 UID (Firebase)
 * @property email 이메일
 * @property displayName 표시 이름
 * @property photoUrl 프로필 사진 URL
 * @property isAuthenticated 인증 여부
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val isAuthenticated: Boolean = false
) 