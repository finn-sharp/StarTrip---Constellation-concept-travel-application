package com.statapp.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.statapp.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 작업을 처리하는 리포지토리 인터페이스입니다.
 */
interface AuthRepository {
    /**
     * 현재 로그인된 사용자를 Flow로 반환합니다.
     */
    fun getCurrentUser(): Flow<User?>

    /**
     * Google 계정으로 로그인합니다.
     * @param account Google 로그인 후 받은 계정 정보
     * @return 성공 시 사용자 정보, 실패 시 null
     */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): User?

    /**
     * 로그아웃합니다.
     */
    suspend fun signOut()

    /**
     * 현재 사용자 인증 상태를 확인합니다.
     * @return 인증 여부
     */
    fun isAuthenticated(): Boolean
} 