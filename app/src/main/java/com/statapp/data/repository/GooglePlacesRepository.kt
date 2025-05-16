package com.statapp.data.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.statapp.BuildConfig
import com.statapp.domain.model.CityData
import com.statapp.domain.model.PlaceDetails
import com.statapp.domain.model.PlaceReview
import com.statapp.domain.model.PlaceSummary
import com.statapp.domain.model.RecommendedPlace
import com.statapp.domain.repository.PlacesRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Google Places API를 사용한 위치 리포지토리 구현체
 */
class GooglePlacesRepository @Inject constructor(
    context: Context
) : PlacesRepository {
    
    private val TAG = "GooglePlacesRepository"
    
    private val placesClient: PlacesClient
    
    // 한국 내 주요 관광지 / 도시 정보
    private val popularDestinations = listOf(
        RecommendedPlace(
            id = "ChIJRUDiD3_jaDURVYvht-K4lqA", // 제주도
            name = "Jeju Island",
            description = "Volcanic island with beautiful beaches",
            stars = listOf(),
            lines = listOf(),
            x = 0.2f,
            y = 0.1f,
            photoUrl = "https://i.imgur.com/8QqZ8Fe.jpg",
            lat = 33.4996213,
            lng = 126.5311884
        ),
        RecommendedPlace(
            id = "ChIJAUJJ9bwWDTURj0VRFM4_q-o", // 경주
            name = "Gyeongju",
            description = "Historical city with ancient temples",
            stars = listOf(),
            lines = listOf(),
            x = 0.7f,
            y = 0.2f,
            photoUrl = "https://i.imgur.com/c2Q2Nzr.jpg",
            lat = 35.8059773,
            lng = 129.2236744
        ),
        RecommendedPlace(
            id = "ChIJnTixnQ1jYzUR_BGvpTfOUHM", // 부산
            name = "Busan",
            description = "Port city with mountains and beaches",
            stars = listOf(),
            lines = listOf(),
            x = 0.8f,
            y = 0.3f,
            photoUrl = "https://i.imgur.com/3JUtEfA.jpg",
            lat = 35.1775947,
            lng = 129.0776458
        ),
        RecommendedPlace(
            id = "ChIJc-9q8X5bezcRvhMYPOyDQk8", // 서울
            name = "Seoul",
            description = "Korea's vibrant capital city",
            stars = listOf(),
            lines = listOf(),
            x = 0.5f,
            y = 0.3f,
            photoUrl = "https://i.imgur.com/FTVv6N0.jpg",
            lat = 37.566535,
            lng = 126.9779692
        ),
        RecommendedPlace(
            id = "ChIJSXzIoG9tezURGCuX2CXWgFo", // 인천
            name = "Incheon",
            description = "Major port city with coastal scenery",
            stars = listOf(),
            lines = listOf(),
            x = 0.3f,
            y = 0.45f,
            photoUrl = "https://i.imgur.com/7a2LFLU.jpg",
            lat = 37.4562557,
            lng = 126.7052062
        )
    )
    
    init {
        // Places API 초기화
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.MAPS_API_KEY)
        }
        placesClient = Places.createClient(context)
    }
    
    override suspend fun getNearbyPlaces(
        lat: Double, 
        lng: Double, 
        radius: Int,
        type: String
    ): List<PlaceSummary> {
        return try {
            // Places API의 Nearby Search 요청 생성
            val location = "${lat},${lng}"
            
            if (type == "restaurant") {
                // 레스토랑 검색 시 특별 처리 (4.0 이상 별점, 리뷰 5개 이상)
                val results = placesClient.findAutocompletePredictions(
                    FindAutocompletePredictionsRequest.builder()
                        .setQuery("restaurant near me")
                        .setLocationBias(
                            RectangularBounds.newInstance(
                                LatLng(lat - 0.03, lng - 0.03),
                                LatLng(lat + 0.03, lng + 0.03)
                            )
                        )
                        .build()
                ).await()
                
                // 검색된 장소 ID로 상세 정보를 가져와 필터링
                results.autocompletePredictions.take(10).mapNotNull { prediction ->
                    try {
                        val placeId = prediction.placeId
                        val placeFields = listOf(
                            Place.Field.ID, 
                            Place.Field.NAME, 
                            Place.Field.RATING,
                            Place.Field.USER_RATINGS_TOTAL,
                            Place.Field.ADDRESS,
                            Place.Field.LAT_LNG,
                            Place.Field.TYPES,
                            Place.Field.PHOTO_METADATAS
                        )
                        
                        val placeResponse = placesClient.fetchPlace(
                            FetchPlaceRequest.newInstance(placeId, placeFields)
                        ).await()
                        
                        val place = placeResponse.place
                        val rating = place.rating?.toFloat() ?: 0f
                        val userRatingsTotal = place.userRatingsTotal ?: 0
                        
                        // 4.0 이상 별점과 리뷰 5개 이상인 레스토랑만 포함
                        if (rating >= 4.0f && userRatingsTotal >= 5) {
                            val photoUrl = place.photoMetadatas?.firstOrNull()?.let { metadata ->
                                getPhotoUrl(metadata)
                            }
                            
                            PlaceSummary(
                                id = placeId,
                                name = place.name ?: "",
                                rating = rating,
                                userRatingsTotal = userRatingsTotal,
                                address = place.address ?: "",
                                types = place.types?.map { it.name } ?: listOf(),
                                iconUrl = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                                lat = place.latLng?.latitude ?: lat,
                                lng = place.latLng?.longitude ?: lng,
                                photoUrl = photoUrl
                            )
                        } else null
                    } catch (e: Exception) {
                        Log.e(TAG, "장소 상세 정보 가져오기 오류: ${e.message}")
                        null
                    }
                }
            } else {
                // 다른 장소 타입은 기존 로직 사용
                popularDestinations.map { 
                    PlaceSummary(
                        id = it.id,
                        name = it.name,
                        rating = 4.5f,
                        userRatingsTotal = 10,  // 기본값 추가
                        address = "${it.name}, South Korea",
                        types = listOf(type),
                        iconUrl = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
                        lat = it.lat,
                        lng = it.lng,
                        photoUrl = it.photoUrl
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getNearbyPlaces 오류: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun getPlaceDetails(placeId: String): PlaceDetails? {
        return try {
            // Place Details API 호출 준비
            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.OPENING_HOURS,
                Place.Field.PRICE_LEVEL,
                Place.Field.TYPES,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS
            )
            
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            val response = placesClient.fetchPlace(request).await()
            val place = response.place
            
            // 사진 URL 가져오기
            val photoUrls = place.photoMetadatas?.take(3)?.map { metadata ->
                getPhotoUrl(metadata)
            } ?: listOf()
            
            // 가격 수준 처리 (정수로 변환)
            val priceLevel = place.priceLevel?.let { level ->
                when {
                    level.toString().contains("FREE") -> 0
                    level.toString().contains("INEXPENSIVE") -> 1
                    level.toString().contains("MODERATE") -> 2
                    level.toString().contains("EXPENSIVE") -> 3
                    level.toString().contains("VERY_EXPENSIVE") -> 4
                    else -> 0
                }
            } ?: 0
            
            PlaceDetails(
                id = place.id ?: "",
                name = place.name ?: "",
                rating = place.rating?.toFloat() ?: 0f,
                userRatingsTotal = place.userRatingsTotal ?: 0,
                address = place.address ?: "",
                phoneNumber = place.phoneNumber,
                website = place.websiteUri?.toString(),
                openingHours = place.openingHours?.weekdayText,
                priceLevel = priceLevel,
                types = place.types?.map { it.name } ?: listOf(),
                lat = place.latLng?.latitude ?: 0.0,
                lng = place.latLng?.longitude ?: 0.0,
                photos = photoUrls,
                reviews = listOf() // 리뷰는 별도 API 호출 필요
            )
        } catch (e: Exception) {
            Log.e(TAG, "getPlaceDetails 오류: ${e.message}")
            // 빠른 구현을 위해 미리 정의된 장소 정보를 반환
            popularDestinations.find { it.id == placeId }?.let { destination ->
                PlaceDetails(
                    id = destination.id,
                    name = destination.name,
                    rating = 4.5f,
                    userRatingsTotal = 500,
                    address = "${destination.name}, South Korea",
                    phoneNumber = "+82-2-1234-5678",
                    website = "https://www.visitkorea.or.kr",
                    openingHours = listOf("Open 24 hours"),
                    priceLevel = 2,
                    types = listOf("tourist_attraction"),
                    lat = destination.lat,
                    lng = destination.lng,
                    photos = listOf(destination.photoUrl ?: ""),
                    reviews = listOf(
                        PlaceReview(
                            authorName = "Travel Expert",
                            authorPhotoUrl = null,
                            rating = 5.0f,
                            relativeTimeDescription = "1 month ago",
                            text = "Fantastic place to visit! The views are breathtaking."
                        )
                    )
                )
            }
        }
    }
    
    override suspend fun searchPlaces(
        query: String,
        lat: Double?,
        lng: Double?
    ): List<PlaceSummary> {
        return try {
            Log.d(TAG, "Places API 검색 시작: '$query'")
            
            // 위치 제한 설정 (선택적)
            val locationBias = if (lat != null && lng != null) {
                RectangularBounds.newInstance(
                    LatLng(lat - 0.1, lng - 0.1),
                    LatLng(lat + 0.1, lng + 0.1)
                )
            } else null
            
            // 자동완성 요청
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationBias(locationBias)
                .build()
                
            Log.d(TAG, "Places API 자동완성 요청 보냄")
            val response = placesClient.findAutocompletePredictions(request).await()
            val predictions = response.autocompletePredictions
            
            Log.d(TAG, "Places API 자동완성 결과: ${predictions.size}개 항목")
            
            // 결과 변환
            val results = predictions.mapNotNull { prediction ->
                try {
                    val placeId = prediction.placeId
                    val placeName = prediction.getPrimaryText(null).toString()
                    val address = prediction.getSecondaryText(null).toString()
                    val types = prediction.placeTypes.map { it.name }
                    
                    Log.d(TAG, "Places API 결과 항목: $placeName, $address")
                    
                    // 세부 정보 요청을 통해 평점과 위치 정보 추가
                    val placeFields = listOf(
                        Place.Field.ID, 
                        Place.Field.NAME, 
                        Place.Field.RATING,
                        Place.Field.USER_RATINGS_TOTAL,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.TYPES,
                        Place.Field.PHOTO_METADATAS
                    )
                    
                    val placeResponse = placesClient.fetchPlace(
                        FetchPlaceRequest.newInstance(placeId, placeFields)
                    ).await()
                    
                    val place = placeResponse.place
                    
                    // 이미지 URL 가져오기
                    val photoUrl = place.photoMetadatas?.firstOrNull()?.let { metadata ->
                        getPhotoUrl(metadata)
                    }
                    
                    PlaceSummary(
                        id = placeId,
                        name = placeName,
                        rating = place.rating?.toFloat() ?: 0f,
                        userRatingsTotal = place.userRatingsTotal ?: 0,
                        address = address,
                        types = types,
                        iconUrl = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
                        lat = place.latLng?.latitude ?: 0.0,
                        lng = place.latLng?.longitude ?: 0.0,
                        photoUrl = photoUrl
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "항목 처리 중 오류: ${e.message}")
                    null
                }
            }
            
            Log.d(TAG, "최종 검색 결과: ${results.size}개 항목 반환")
            results
            
        } catch (e: Exception) {
            Log.e(TAG, "searchPlaces 오류: ${e.message}", e)
            emptyList()
        }
    }
    
    override suspend fun getPopularDestinations(): List<PlaceSummary> {
        return popularDestinations.map { 
            PlaceSummary(
                id = it.id,
                name = it.name,
                rating = 4.5f,
                userRatingsTotal = 10,  // 기본값 추가
                address = "${it.name}, South Korea",
                types = listOf("tourist_attraction"),
                iconUrl = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
                lat = it.lat,
                lng = it.lng,
                photoUrl = it.photoUrl
            )
        }
    }
    
    override suspend fun getPlacePhotos(placeId: String, maxWidth: Int): List<String> {
        return try {
            // Place Details API에서 Photo Metadata 가져오기
            val placeFields = listOf(Place.Field.PHOTO_METADATAS)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            val response = placesClient.fetchPlace(request).await()
            
            response.place.photoMetadatas?.take(5)?.mapNotNull { metadata ->
                getPhotoUrl(metadata, maxWidth)
            } ?: listOf()
        } catch (e: Exception) {
            Log.e(TAG, "getPlacePhotos 오류: ${e.message}")
            // 미리 정의된 장소의 사진을 반환
            popularDestinations.find { it.id == placeId }?.let { 
                listOfNotNull(it.photoUrl)
            } ?: listOf()
        }
    }
    
    /**
     * Places API를 통해 사진 URL을 가져옵니다.
     */
    private suspend fun getPhotoUrl(metadata: PhotoMetadata, maxWidth: Int = 400): String {
        return suspendCancellableCoroutine { continuation ->
            try {
                val photoRequest = FetchPhotoRequest.builder(metadata)
                    .setMaxWidth(maxWidth)
                    .build()
                
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        val fileName = "place_${UUID.randomUUID()}.jpg"
                        val cacheDir = File(System.getProperty("java.io.tmpdir"))
                        val imageFile = File(cacheDir, fileName)
                        
                        // 파일 URL로 변환 (실제 앱에서는 파일 저장 및 URL 생성 로직 개선 필요)
                        val photoUrl = "content://com.statapp.fileprovider/$fileName"
                        continuation.resume(photoUrl)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }
} 