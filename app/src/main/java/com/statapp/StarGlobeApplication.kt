package com.statapp

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * 애플리케이션 클래스 - Hilt 의존성 주입을 위해 필요합니다.
 * 앱 시작 시 Firebase 초기화도 담당합니다.
 */
@HiltAndroidApp
class StarGlobeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Firebase 초기화
        FirebaseApp.initializeApp(this)
    }
} 