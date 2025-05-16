package com.statapp.domain.usecase.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.statapp.domain.model.User
import com.statapp.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Google 계정으로 로그인하는 UseCase입니다.
 */
class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Google 계정으로 로그인을 수행합니다.
     * @param account Google 로그인 후 받은 계정 정보
     * @return 성공 시 사용자 정보, 실패 시 null
     */
    suspend operator fun invoke(account: GoogleSignInAccount): User? {
        return authRepository.signInWithGoogle(account)
    }
} 