package com.statapp.domain.usecase.star

import com.statapp.domain.model.Star
import com.statapp.domain.repository.StarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 별(장소)의 활성화 상태를 업데이트하는 유스케이스입니다.
 *
 * @property starRepository Star 데이터 관련 작업을 수행하는 레포지토리
 */
class UpdateStarStatusUseCase @Inject constructor(
    private val starRepository: StarRepository
) {
    /**
     * 특정 별(장소)의 활성화 상태를 업데이트합니다.
     *
     * @param id 상태를 변경할 별의 ID
     * @param isActive 설정할 활성화 상태
     * @return 성공 여부
     */
    suspend operator fun invoke(id: Long, isActive: Boolean): Boolean {
        starRepository.updateStarStatus(id, isActive)
        return true
    }
} 