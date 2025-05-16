package com.statapp.data.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.statapp.domain.model.User
import com.statapp.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * AuthRepository 인터페이스의 실제 구현체입니다.
 * Firebase 인증 및 Google 로그인을 처리합니다.
 */
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) : AuthRepository {

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                Log.d("AuthRepository", "인증 상태 변경 감지: 로그인됨 (${firebaseUser.email})")
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                    isAuthenticated = true
                )
                trySend(user)
            } else {
                Log.d("AuthRepository", "인증 상태 변경 감지: 로그아웃됨")
                trySend(null)
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        // 초기값
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            Log.d("AuthRepository", "초기 인증 상태: 로그인됨 (${firebaseUser.email})")
            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                isAuthenticated = true
            )
            trySend(user)
        } else {
            Log.d("AuthRepository", "초기 인증 상태: 로그아웃됨")
            trySend(null)
        }

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): User? {
        try {
            Log.d("AuthRepository", "Google 계정으로 로그인 시도: ${account.email}")
            val idToken = account.idToken
            
            if (idToken == null) {
                Log.e("AuthRepository", "ID 토큰이 null입니다")
                return null
            }
            
            Log.d("AuthRepository", "Google 계정의 ID 토큰 가져옴")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            
            Log.d("AuthRepository", "Firebase에 자격 증명으로 로그인 시도")
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            
            val firebaseUser = authResult.user
            if (firebaseUser == null) {
                Log.e("AuthRepository", "Firebase 사용자가 null입니다")
                return null
            }
            
            Log.d("AuthRepository", "Firebase 로그인 성공: ${firebaseUser.email}")
            return User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                isAuthenticated = true
            )
        } catch (e: Exception) {
            Log.e("AuthRepository", "로그인 실패: ${e.message}", e)
            e.printStackTrace()
            return null
        }
    }

    override suspend fun signOut() {
        Log.d("AuthRepository", "로그아웃 시도")
        googleSignInClient.signOut().await()
        firebaseAuth.signOut()
        Log.d("AuthRepository", "로그아웃 완료")
    }

    override fun isAuthenticated(): Boolean {
        val isAuth = firebaseAuth.currentUser != null
        Log.d("AuthRepository", "인증 상태 확인: $isAuth")
        return isAuth
    }
} 