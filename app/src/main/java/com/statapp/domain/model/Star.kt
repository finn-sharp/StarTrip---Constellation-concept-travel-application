package com.statapp.domain.model

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * 천문학적 별 데이터를 나타내는 도메인 모델 클래스입니다.
 *
 * @property id 별 식별자
 * @property name 별의 이름
 * @property latitude 위도 좌표
 * @property longitude 경도 좌표
 * @property magnitude 별의 밝기 (절대 등급)
 * @property distance 지구로부터의 거리 (광년)
 * @property isActive 별의 활성화 상태
 * @property type 별의 종류 (예: G형 항성, 적색 거성 등)
 * @property constellation 소속된 별자리
 */
data class AstronomicalStar(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val distance: Double,
    val isActive: Boolean,
    val type: String,
    val constellation: String
)

/**
 * 별(장소)의 도메인 모델입니다.
 *
 * @property id 별의 고유 ID
 * @property placeId Google Place ID
 * @property name 장소 이름
 * @property latitude 위도
 * @property longitude 경도
 * @property rating 평점
 * @property businessHours 영업 시간 정보
 * @property isActive 현재 활성 상태 (영업중 여부)
 * @property isFavorite 즐겨찾기 여부
 */
data class Star(
    val id: Long = 0,
    val placeId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val businessHours: Map<DayOfWeek, BusinessHours> = emptyMap(),
    val isActive: Boolean = false,
    val isFavorite: Boolean = false
)

/**
 * 영업 시간 정보
 *
 * @property openTime 개점 시간
 * @property closeTime 폐점 시간
 * @property isOpen24Hours 24시간 영업 여부
 * @property isClosed 휴무일 여부
 */
data class BusinessHours(
    val openTime: LocalTime? = null,
    val closeTime: LocalTime? = null,
    val isOpen24Hours: Boolean = false,
    val isClosed: Boolean = false
) 