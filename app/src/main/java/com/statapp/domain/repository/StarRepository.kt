package com.statapp.domain.repository

import com.statapp.domain.model.Star
import kotlinx.coroutines.flow.Flow

/**
 * 별(장소) 관련 작업을 처리하는 리포지토리 인터페이스입니다.
 */
interface StarRepository {
    /**
     * 모든 별 목록을 Flow로 반환합니다.
     */
    fun getStars(): Flow<List<Star>>

    /**
     * ID로 별을 조회합니다.
     * @param id 별 ID
     */
    suspend fun getStarById(id: Long): Star?

    /**
     * 현재 활성화된 별만 조회합니다.
     */
    fun getActiveStars(): Flow<List<Star>>

    /**
     * 새로운 별을 추가합니다.
     * @param star 추가할 별 정보
     * @return 추가된 별의 ID
     */
    suspend fun addStar(star: Star): Long

    /**
     * 별을 삭제합니다.
     * @param id 삭제할 별 ID
     */
    suspend fun deleteStar(id: Long)

    /**
     * 별의 활성 상태를 업데이트합니다.
     * @param id 업데이트할 별 ID
     * @param isActive 활성 상태 여부
     */
    suspend fun updateStarStatus(id: Long, isActive: Boolean)

    /**
     * 즐겨찾기 상태를 업데이트합니다.
     * @param id 업데이트할 별 ID
     * @param isFavorite 즐겨찾기 여부
     */
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
} 