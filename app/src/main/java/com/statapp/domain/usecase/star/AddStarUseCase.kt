package com.statapp.domain.usecase.star

import com.statapp.domain.model.Star
import com.statapp.domain.repository.StarRepository
import javax.inject.Inject

/**
 * 새로운 별을 추가하는 UseCase입니다.
 */
class AddStarUseCase @Inject constructor(
    private val starRepository: StarRepository
) {
    /**
     * 새로운 별을 추가합니다.
     * @param star 추가할 별 정보
     * @return 추가된 별의 ID
     */
    suspend operator fun invoke(star: Star): Long {
        return starRepository.addStar(star)
    }
} 