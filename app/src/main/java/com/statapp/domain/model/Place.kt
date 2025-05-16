package com.statapp.domain.model

import androidx.compose.ui.geometry.Offset

/**
 * 장소 요약 정보 (목록 표시용)
 */
data class PlaceSummary(
    val id: String,
    val name: String,
    val rating: Float,
    val userRatingsTotal: Int = 0,
    val address: String,
    val types: List<String>,
    val iconUrl: String,
    val lat: Double,
    val lng: Double,
    val photoUrl: String? = null,
    val isPinned: Boolean = false
)

/**
 * 장소 상세 정보
 */
data class PlaceDetails(
    val id: String,
    val name: String,
    val rating: Float,
    val userRatingsTotal: Int,
    val address: String,
    val phoneNumber: String?,
    val website: String?,
    val openingHours: List<String>?,
    val priceLevel: Int?,
    val types: List<String>,
    val lat: Double,
    val lng: Double,
    val photos: List<String>,
    val reviews: List<PlaceReview>
)

/**
 * 장소 리뷰 정보
 */
data class PlaceReview(
    val authorName: String,
    val authorPhotoUrl: String?,
    val rating: Float,
    val relativeTimeDescription: String,
    val text: String
)

/**
 * 추천 장소 데이터 (별자리 표현용)
 */
data class RecommendedPlace(
    val id: String = "",
    val name: String,
    val description: String,
    val stars: List<Offset>,
    val lines: List<Pair<Int, Int>>,
    val x: Float,
    val y: Float,
    val photoUrl: String? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

/**
 * 도시 데이터 클래스
 */
data class CityData(
    val name: String,
    val x: Float,
    val y: Float,
    val lat: Double = 0.0,
    val lng: Double = 0.0
) 