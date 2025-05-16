package com.statapp.di

import com.statapp.data.local.StarDatabase
import com.statapp.data.repository.StarRepositoryImpl
import com.statapp.domain.repository.StarRepository
import com.statapp.domain.usecase.star.AddStarUseCase
import com.statapp.domain.usecase.star.GetStarsUseCase
import com.statapp.domain.usecase.star.UpdateStarStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 별 관련 의존성을 제공하는 Hilt 모듈입니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object StarModule {

    /**
     * StarRepository 구현체를 제공합니다.
     */
    @Provides
    @Singleton
    fun provideStarRepository(database: StarDatabase): StarRepository {
        return StarRepositoryImpl(database.starDao())
    }

    /**
     * 별 추가 UseCase를 제공합니다.
     */
    @Provides
    fun provideAddStarUseCase(repository: StarRepository): AddStarUseCase {
        return AddStarUseCase(repository)
    }

    /**
     * 별 목록 조회 UseCase를 제공합니다.
     */
    @Provides
    fun provideGetStarsUseCase(repository: StarRepository): GetStarsUseCase {
        return GetStarsUseCase(repository)
    }

    /**
     * 별 상태 업데이트 UseCase를 제공합니다.
     */
    @Provides
    fun provideUpdateStarStatusUseCase(repository: StarRepository): UpdateStarStatusUseCase {
        return UpdateStarStatusUseCase(repository)
    }
} 