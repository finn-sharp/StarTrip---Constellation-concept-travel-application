package com.statapp.domain.repository

import com.statapp.domain.model.PlaceDetails
import com.statapp.domain.model.PlaceSummary
import kotlinx.coroutines.flow.Flow

/**
 * Google Places API를 통해 장소 정보를 제공하는 리포지토리 인터페이스
 */
interface PlacesRepository {
    
    /**
     * 현재 위치 주변의 추천 장소를 가져옵니다.
     * @param lat 현재 위도
     * @param lng 현재 경도
     * @param radius 검색 반경 (미터)
     * @param type 장소 유형 (예: "tourist_attraction", "restaurant" 등)
     * @return 추천 장소 목록
     */
    suspend fun getNearbyPlaces(
        lat: Double, 
        lng: Double, 
        radius: Int = 5000,
        type: String = "tourist_attraction"
    ): List<PlaceSummary>
    
    /**
     * 장소 세부 정보를 가져옵니다.
     * @param placeId Google Places API의 장소 ID
     * @return 장소 세부 정보
     */
    suspend fun getPlaceDetails(placeId: String): PlaceDetails?
    
    /**
     * 장소 이름이나 키워드로 검색합니다.
     * @param query 검색어
     * @param lat 현재 위도 (선택 사항)
     * @param lng 현재 경도 (선택 사항)
     * @return 검색 결과 목록
     */
    suspend fun searchPlaces(
        query: String,
        lat: Double? = null,
        lng: Double? = null
    ): List<PlaceSummary>
    
    /**
     * 인기 있는 여행지를 가져옵니다.
     * @return 인기 여행지 목록
     */
    suspend fun getPopularDestinations(): List<PlaceSummary>
    
    /**
     * 장소에 대한 사진 URL들을 가져옵니다.
     * @param placeId Google Places API의 장소 ID
     * @param maxWidth 요청할 최대 이미지 너비
     * @return 사진 URL 목록
     */
    suspend fun getPlacePhotos(placeId: String, maxWidth: Int = 400): List<String>
} 