package com.statapp.data.repository

import com.statapp.data.local.StarDao
import com.statapp.data.mapper.toDomain
import com.statapp.data.mapper.toEntity
import com.statapp.domain.model.Star
import com.statapp.domain.repository.StarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * StarRepository 인터페이스의 실제 구현체입니다.
 * Room 데이터베이스와 통신합니다.
 */
class StarRepositoryImpl @Inject constructor(
    private val starDao: StarDao
) : StarRepository {

    override fun getStars(): Flow<List<Star>> {
        return starDao.getStars().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getStarById(id: Long): Star? {
        return starDao.getStarById(id)?.toDomain()
    }

    override fun getActiveStars(): Flow<List<Star>> {
        return starDao.getActiveStars().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addStar(star: Star): Long {
        return starDao.insertStar(star.toEntity())
    }

    override suspend fun deleteStar(id: Long) {
        starDao.deleteStar(id)
    }

    override suspend fun updateStarStatus(id: Long, isActive: Boolean) {
        starDao.updateStarStatus(id, isActive)
    }

    override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
        starDao.updateFavoriteStatus(id, isFavorite)
    }
} 