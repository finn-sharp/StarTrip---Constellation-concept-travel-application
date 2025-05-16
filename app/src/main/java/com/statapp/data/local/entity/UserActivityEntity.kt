package com.statapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.statapp.domain.model.UserActivity
import java.util.UUID

/**
 * 사용자 활동을 위한 Room 엔티티
 */
@Entity(
    tableName = "user_activities",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class UserActivityEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val type: String,
    val timestamp: Long,
    val dataJson: String  // JSON 형태로 변환된 데이터
) {
    companion object {
        /**
         * 도메인 모델을 엔티티로 변환
         */
        fun fromDomain(activity: UserActivity): UserActivityEntity {
            val activityId = activity.id.ifEmpty { UUID.randomUUID().toString() }
            return UserActivityEntity(
                id = activityId,
                userId = activity.userId,
                type = activity.type,
                timestamp = activity.timestamp,
                dataJson = activity.data.toString()  // 실제 구현에서는 JSON 라이브러리 사용
            )
        }
    }
    
    /**
     * 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): UserActivity {
        // 실제 구현에서는 JSON 라이브러리를 사용하여 dataJson을 Map으로 변환
        return UserActivity(
            id = id,
            userId = userId,
            type = type,
            timestamp = timestamp,
            data = mapOf()  // 단순 구현
        )
    }
} 