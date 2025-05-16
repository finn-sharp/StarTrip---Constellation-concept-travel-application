package com.statapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.statapp.data.repository.FirestoreUserRepository
import com.statapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 데이터 관련 의존성을 제공하는 Hilt 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    /**
     * Firebase Firestore 인스턴스를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore
    
    /**
     * UserRepository 구현체를 제공합니다.
     * Room DB 구현으로 대체되어 주석 처리됨
     */
    /*
    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return FirestoreUserRepository(firestore)
    }
    */
} 