package com.statapp.presentation.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.statapp.domain.model.User
import com.statapp.domain.model.UserActivity
import com.statapp.domain.model.UserData
import com.statapp.domain.repository.AuthRepository
import com.statapp.domain.repository.UserRepository
import com.statapp.domain.usecase.auth.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면의 ViewModel입니다.
 * Google 로그인 및 인증 상태 관리를 담당합니다.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val TAG = "LoginViewModel"

    // 로그인 프로세스 상태
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // 현재 인증 상태 (로그인 여부)
    val isAuthenticated: StateFlow<Boolean> = authRepository.getCurrentUser()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = authRepository.isAuthenticated()
        )

    // 로그인한 사용자 정보
    val currentUser: StateFlow<User?> = authRepository.getCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // 인증 상태를 검증하고 현재 사용자 정보를 확인합니다
        viewModelScope.launch {
            if (authRepository.isAuthenticated()) {
                Log.d(TAG, "초기화: 사용자가 이미 인증되어 있습니다")
            } else {
                Log.d(TAG, "초기화: 인증된 사용자가 없습니다")
            }
        }
    }

    /**
     * Google 계정으로 로그인합니다.
     * @param account Google 로그인 후 받은 계정 정보
     */
    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            Log.d(TAG, "로그인 시작: ${account.email}")
            _loginState.value = LoginState.Loading
            
            try {
                Log.d(TAG, "SignInWithGoogleUseCase 호출")
                val user = signInWithGoogleUseCase(account)
                
                if (user != null) {
                    Log.d(TAG, "Firebase 로그인 성공: ${user.email}, isAuthenticated=${authRepository.isAuthenticated()}")
                    
                    // 사용자 데이터를 저장합니다
                    val userDataSaved = saveUserData(user)
                    
                    if (userDataSaved) {
                        // 모든 프로세스가 성공적으로 완료됨
                        Log.d(TAG, "모든 로그인 프로세스 완료: ${user.email}")
                        _loginState.value = LoginState.Success(user)
                    } else {
                        // Firebase 인증은 성공했지만 로컬 데이터 저장에 실패했을 경우
                        // 그래도 로그인은 성공했다고 간주
                        Log.w(TAG, "인증은 성공했지만 사용자 데이터 저장 실패: ${user.email}")
                        _loginState.value = LoginState.Success(user)
                    }
                } else {
                    Log.e(TAG, "로그인 실패: 사용자 정보가 null입니다")
                    _loginState.value = LoginState.Error("로그인에 실패했습니다.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "로그인 오류: ${e.message}", e)
                _loginState.value = LoginState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }
    
    /**
     * 사용자 데이터를 로컬 데이터베이스(Room)에 저장합니다.
     * @return 저장 성공 여부
     */
    private suspend fun saveUserData(user: User): Boolean {
        return try {
            Log.d(TAG, "사용자 데이터 로컬 DB에 저장: ${user.uid}")
            
            // 사용자 기본 정보 저장
            val userData = UserData(
                userId = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl,
                lastLoginAt = System.currentTimeMillis()
            )
            val saved = userRepository.updateUserData(userData)
            
            if (saved) {
                Log.d(TAG, "사용자 데이터 저장 성공")
                
                // 로그인 활동 기록
                val activity = UserActivity(
                    userId = user.uid,
                    type = "login",
                    data = mapOf("method" to "google")
                )
                userRepository.saveUserActivity(user.uid, activity)
                true
            } else {
                Log.e(TAG, "사용자 데이터 저장 실패")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "사용자 데이터 저장 중 오류: ${e.message}", e)
            false
        }
    }

    /**
     * Google 로그인 인텐트를 가져옵니다.
     * @return Google 로그인을 위한 인텐트
     */
    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    /**
     * 로그아웃합니다.
     */
    fun signOut() {
        viewModelScope.launch {
            Log.d(TAG, "로그아웃 시도")
            try {
                authRepository.signOut()
                _loginState.value = LoginState.Initial
                Log.d(TAG, "로그아웃 완료")
            } catch (e: Exception) {
                Log.e(TAG, "로그아웃 중 오류: ${e.message}", e)
                _loginState.value = LoginState.Error("로그아웃 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }
}

/**
 * 로그인 프로세스의 상태입니다.
 */
sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
} 