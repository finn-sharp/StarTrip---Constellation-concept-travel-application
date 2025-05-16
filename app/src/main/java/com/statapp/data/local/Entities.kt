package com.statapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Room 데이터베이스에서 사용할 Star Entity 클래스입니다.
 */
@Entity(tableName = "stars")
@TypeConverters(BusinessHoursConverter::class)
data class StarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val placeId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val businessHours: Map<DayOfWeek, BusinessHoursEntity> = emptyMap(),
    val isActive: Boolean = false,
    val isFavorite: Boolean = false
)

/**
 * 영업 시간 정보를 저장하는 Entity 클래스입니다.
 */
data class BusinessHoursEntity(
    val openTimeMinutes: Int? = null, // LocalTime을 분 단위로 저장
    val closeTimeMinutes: Int? = null, // LocalTime을 분 단위로 저장
    val isOpen24Hours: Boolean = false,
    val isClosed: Boolean = false
)

/**
 * 별자리 Entity 클래스입니다.
 */
@Entity(tableName = "constellations")
data class ConstellationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val userId: String // 사용자 구분용
)

/**
 * 별자리-별 관계를 저장하는 Entity 클래스입니다.
 */
@Entity(
    tableName = "constellation_stars",
    primaryKeys = ["constellationId", "starId"]
)
data class ConstellationStarCrossRef(
    val constellationId: Long,
    val starId: Long
)

/**
 * 별자리 내 별들의 연결 정보를 저장하는 Entity 클래스입니다.
 */
@Entity(
    tableName = "star_connections",
    primaryKeys = ["constellationId", "firstStarId", "secondStarId"]
)
data class StarConnectionEntity(
    val constellationId: Long,
    val firstStarId: Long,
    val secondStarId: Long
)

/**
 * BusinessHours 타입과 String 간 변환을 위한 TypeConverter입니다.
 */
class BusinessHoursConverter {
    private val gson = Gson()

    /**
     * Map을 JSON 문자열로 변환합니다.
     */
    @TypeConverter
    fun fromBusinessHours(businessHours: Map<DayOfWeek, BusinessHoursEntity>): String {
        return gson.toJson(businessHours.mapKeys { it.key.name })
    }

    /**
     * JSON 문자열을 Map으로 변환합니다.
     */
    @TypeConverter
    fun toBusinessHours(json: String): Map<DayOfWeek, BusinessHoursEntity> {
        val type = object : TypeToken<Map<String, BusinessHoursEntity>>() {}.type
        val stringMap: Map<String, BusinessHoursEntity> = gson.fromJson(json, type)
        
        return stringMap.mapKeys { DayOfWeek.valueOf(it.key) }
    }
} 
 