package com.statapp.domain.usecase.star

import com.statapp.domain.model.Star
import com.statapp.domain.repository.StarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 모든 별 목록을 조회하는 UseCase입니다.
 */
class GetStarsUseCase @Inject constructor(
    private val starRepository: StarRepository
) {
    /**
     * 모든 별 목록을 Flow로 반환합니다.
     */
    operator fun invoke(): Flow<List<Star>> {
        return starRepository.getStars()
    }
} 