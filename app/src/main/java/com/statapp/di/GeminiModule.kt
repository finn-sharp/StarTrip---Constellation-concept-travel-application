package com.statapp.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.statapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Google Gemini AI 기능 관련 의존성을 제공하는 Hilt 모듈입니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    /**
     * Gemini Pro 모델 인스턴스를 제공합니다.
     * 별자리 추천 기능 등에 사용됩니다.
     */
    @Provides
    @Singleton
    fun provideGeminiProModel(): GenerativeModel {
        // BuildConfig에서 Gemini API 키 사용
        val apiKey = BuildConfig.GEMINI_API_KEY
        
        // Gemini Pro 모델 생성
        return GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }
} 