package com.statapp.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.statapp.BuildConfig
import com.statapp.data.repository.AuthRepositoryImpl
import com.statapp.domain.repository.AuthRepository
import com.statapp.domain.usecase.auth.SignInWithGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 인증 관련 의존성을 제공하는 Hilt 모듈입니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    /**
     * Firebase Auth 인스턴스를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    /**
     * Google 로그인 클라이언트를 제공합니다.
     * 웹 클라이언트 ID는 Firebase Console에서 얻을 수 있습니다.
     */
    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        // 웹 클라이언트 ID 사용 (Firebase Console > 프로젝트 설정 > 일반 > 웹 API 키)
        // 참고: google-services.json 파일의 client > oauth_client 중 type이 3인 항목의 client_id
        val defaultWebClientId = context.getString(com.statapp.R.string.default_web_client_id)
        
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId) // Firebase 웹 클라이언트 ID
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    /**
     * AuthRepository 구현체를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, googleSignInClient)
    }

    /**
     * Google 로그인 UseCase를 제공합니다.
     */
    @Provides
    fun provideSignInWithGoogleUseCase(repository: AuthRepository): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(repository)
    }
} 