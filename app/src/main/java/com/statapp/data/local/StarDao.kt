package com.statapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 별(장소) 데이터에 접근하기 위한 DAO(Data Access Object)입니다.
 */
@Dao
interface StarDao {
    /**
     * 모든 별 목록을 조회합니다.
     */
    @Query("SELECT * FROM stars")
    fun getStars(): Flow<List<StarEntity>>

    /**
     * ID로 별을 조회합니다.
     * @param id 별 ID
     */
    @Query("SELECT * FROM stars WHERE id = :id")
    suspend fun getStarById(id: Long): StarEntity?

    /**
     * 활성화된 별만 조회합니다.
     */
    @Query("SELECT * FROM stars WHERE isActive = 1")
    fun getActiveStars(): Flow<List<StarEntity>>

    /**
     * 새로운 별을 추가합니다.
     * @param star 추가할 별 Entity
     * @return 추가된 별의 ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStar(star: StarEntity): Long

    /**
     * 별을 삭제합니다.
     * @param id 삭제할 별 ID
     */
    @Query("DELETE FROM stars WHERE id = :id")
    suspend fun deleteStar(id: Long)

    /**
     * 별의 활성 상태를 업데이트합니다.
     * @param id 업데이트할 별 ID
     * @param isActive 활성 상태 여부
     */
    @Query("UPDATE stars SET isActive = :isActive WHERE id = :id")
    suspend fun updateStarStatus(id: Long, isActive: Boolean)

    /**
     * 즐겨찾기 상태를 업데이트합니다.
     * @param id 업데이트할 별 ID
     * @param isFavorite 즐겨찾기 여부
     */
    @Query("UPDATE stars SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
} 