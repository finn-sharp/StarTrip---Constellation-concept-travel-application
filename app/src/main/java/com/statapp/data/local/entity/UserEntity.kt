package com.statapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.statapp.domain.model.UserData

/**
 * 사용자 정보를 위한 Room 엔티티
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val createdAt: Long,
    val lastLoginAt: Long
) {
    companion object {
        /**
         * 도메인 모델을 엔티티로 변환
         */
        fun fromDomain(userData: UserData): UserEntity {
            return UserEntity(
                userId = userData.userId,
                email = userData.email,
                displayName = userData.displayName,
                photoUrl = userData.photoUrl,
                createdAt = userData.createdAt,
                lastLoginAt = userData.lastLoginAt
            )
        }
    }
    
    /**
     * 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): UserData {
        return UserData(
            userId = userId,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            createdAt = createdAt,
            lastLoginAt = lastLoginAt
        )
    }
} 