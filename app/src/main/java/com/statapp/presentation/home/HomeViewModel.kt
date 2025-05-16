package com.statapp.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.statapp.domain.model.CityData
import com.statapp.domain.model.PlaceSummary
import com.statapp.domain.model.RecommendedPlace
import com.statapp.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 홈 화면의 ViewModel입니다.
 * 별 목록, 지구본/지도 상태, 사용자 정보 등을 관리합니다.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = "HomeViewModel"
    
    // 위치 제공자
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    // 인기 관광지 정보 (Gemini 추천 장소)
    private val _recommendedPlaces = MutableStateFlow<List<RecommendedPlace>>(emptyList())
    val recommendedPlaces: StateFlow<List<RecommendedPlace>> = _recommendedPlaces.asStateFlow()
    
    // 도시 정보
    private val _cities = MutableStateFlow<List<CityData>>(emptyList())
    val cities: StateFlow<List<CityData>> = _cities.asStateFlow()
    
    // 현재 검색된 장소 목록
    private val _searchResults = MutableStateFlow<List<PlaceSummary>>(emptyList())
    val searchResults: StateFlow<List<PlaceSummary>> = _searchResults.asStateFlow()
    
    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 오류 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // 별자리 좌표 (하드코딩된 값)
    private val starPatterns = listOf(
        // 제주도 별자리 패턴
        listOf(
            Offset(120f, 80f),
            Offset(180f, 60f),
            Offset(220f, 100f),
            Offset(160f, 140f)
        ),
        // 경주 별자리 패턴
        listOf(
            Offset(50f, 50f),
            Offset(90f, 80f),
            Offset(70f, 120f),
            Offset(30f, 90f)
        ),
        // 부산 별자리 패턴
        listOf(
            Offset(30f, 30f),
            Offset(60f, 20f),
            Offset(80f, 50f),
            Offset(50f, 70f),
            Offset(20f, 60f)
        ),
        // 서울 별자리 패턴
        listOf(
            Offset(100f, 120f),
            Offset(130f, 90f),
            Offset(160f, 110f),
            Offset(140f, 150f),
            Offset(110f, 140f)
        ),
        // 인천 별자리 패턴
        listOf(
            Offset(180f, 180f),
            Offset(210f, 160f),
            Offset(230f, 190f),
            Offset(200f, 220f),
            Offset(170f, 200f)
        )
    )
    
    // 별자리 연결선 (하드코딩된 값)
    private val starLines = listOf(
        // 제주도 별자리 라인
        listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 0)
        ),
        // 경주 별자리 라인
        listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 0)
        ),
        // 부산 별자리 라인
        listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 4),
            Pair(4, 0)
        ),
        // 서울 별자리 라인
        listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 4),
            Pair(4, 0)
        ),
        // 인천 별자리 라인
        listOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 4),
            Pair(4, 0)
        )
    )
    
    init {
        loadRecommendedPlaces()
    }
    
    /**
     * 인기 관광지 정보를 Google Places API에서 로드합니다.
     */
    fun loadRecommendedPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val places = placesRepository.getPopularDestinations()
                
                // 장소 정보를 RecommendedPlace 형식으로 변환
                val recommendedPlaces = places.mapIndexed { index, place ->
                    val starPattern = if (index < starPatterns.size) starPatterns[index] else starPatterns[0]
                    val lines = if (index < starLines.size) starLines[index] else starLines[0]
                    
                    RecommendedPlace(
                        id = place.id,
                        name = place.name,
                        description = place.address,
                        stars = starPattern,
                        lines = lines,
                        x = 0.1f + (index * 0.2f), // 화면 내 위치 분산
                        y = 0.1f + (index * 0.1f),
                        photoUrl = place.photoUrl,
                        lat = place.lat,
                        lng = place.lng
                    )
                }
                
                _recommendedPlaces.value = recommendedPlaces
                
                // 도시 정보 생성
                val cityData = places.mapIndexed { index, place ->
                    CityData(
                        name = place.name,
                        x = 0.2f + (index * 0.15f), // 화면 내 위치 분산
                        y = 0.3f + (index * 0.12f),
                        lat = place.lat,
                        lng = place.lng
                    )
                }
                
                _cities.value = cityData
            } catch (e: Exception) {
                Log.e(TAG, "인기 장소 로드 오류: ${e.message}")
                _errorMessage.value = "인기 장소를 로드하지 못했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 키워드로 장소 검색
     */
    fun searchPlaces(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
                return@launch
            }
            
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                Log.d(TAG, "장소 검색 시작: '$query'")
                val results = placesRepository.searchPlaces(query)
                if (results.isEmpty()) {
                    Log.d(TAG, "검색 결과 없음: '$query'")
                    _errorMessage.value = "'$query'에 대한 검색 결과가 없습니다."
                } else {
                    Log.d(TAG, "검색 결과 ${results.size}개 찾음: '$query'")
                    _searchResults.value = results
                }
            } catch (e: Exception) {
                Log.e(TAG, "장소 검색 오류: ${e.message}", e)
                _errorMessage.value = "장소 검색 중 오류가 발생했습니다: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 현재 위치 주변의 관광지 로드
     */
    fun loadNearbyPlaces(lat: Double, lng: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val nearby = placesRepository.getNearbyPlaces(lat, lng)
                
                // 검색 결과가 있는 경우 결과 설정
                if (nearby.isNotEmpty()) {
                    _searchResults.value = nearby
                }
            } catch (e: Exception) {
                Log.e(TAG, "주변 장소 로드 오류: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 현재 위치 주변의 맛집 로드 (최소 결과 수 보장)
     * 별점과 리뷰 수 필터링을 적용하되, 최소 결과 수를 보장합니다.
     */
    fun loadNearbyRestaurantsWithMinResults(
        lat: Double, 
        lng: Double, 
        minRating: Double = 4.0, 
        minReviews: Int = 5,
        minResultCount: Int = 3
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // 첫 번째 시도: 높은 필터링 조건으로 시도
                val nearby = placesRepository.getNearbyPlaces(
                    lat = lat,
                    lng = lng,
                    radius = 3000,
                    type = "restaurant"
                ).filter { 
                    it.rating >= minRating && it.userRatingsTotal >= minReviews 
                }
                
                if (nearby.size >= minResultCount) {
                    // 충분한 결과가 있으면 그대로 사용
                    _searchResults.value = nearby
                } else {
                    // 결과가 부족하면 필터링 조건을 낮춰서 다시 시도
                    val moreResults = placesRepository.getNearbyPlaces(
                        lat = lat,
                        lng = lng,
                        radius = 5000, // 반경 확대
                        type = "restaurant"
                    ).filter {
                        it.rating >= minRating - 0.5 && it.userRatingsTotal >= minReviews - 2
                    }
                    
                    if (moreResults.size >= minResultCount) {
                        _searchResults.value = moreResults
                    } else {
                        // 그래도 부족하면 필터링 없이 모든 결과 사용
                        val allResults = placesRepository.getNearbyPlaces(
                            lat = lat,
                            lng = lng,
                            radius = 8000, // 더 넓은 반경
                            type = "restaurant"
                        )
                        
                        // 최소한 3개 이상 보장
                        if (allResults.isNotEmpty()) {
                            _searchResults.value = allResults.take(
                                maxOf(minResultCount, allResults.size)
                            )
                        } else {
                            // 백업 데이터 - 결과가 없는 경우 기본 추천 표시
                            loadRecommendedPlaces()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "주변 맛집 로드 오류: ${e.message}")
                _errorMessage.value = "주변 맛집을 로드하지 못했습니다: ${e.message}"
                // 오류 발생 시 기본 추천 장소 표시
                loadRecommendedPlaces()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 현재 위치 주변의 특정 카테고리 장소 검색 (최소 결과 수 보장)
     */
    fun searchNearbyPlacesWithMinResults(
        lat: Double, 
        lng: Double, 
        category: String, 
        minRating: Double = 4.0, 
        minReviews: Int = 5,
        minResultCount: Int = 3
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // 카테고리에 따른 타입 매핑
                val placeType = when (category) {
                    "Restaurants" -> "restaurant"
                    "Cafes" -> "cafe"
                    "Tourist Attractions" -> "tourist_attraction"
                    "Hotels" -> "lodging"
                    "Shopping" -> "shopping_mall"
                    else -> ""
                }
                
                // 첫 번째 시도: 높은 필터링 조건으로 시도
                val nearby = placesRepository.getNearbyPlaces(
                    lat = lat,
                    lng = lng,
                    radius = 3000,
                    type = placeType
                ).filter { 
                    it.rating >= minRating && it.userRatingsTotal >= minReviews 
                }
                
                if (nearby.size >= minResultCount) {
                    // 충분한 결과가 있으면 그대로 사용
                    _searchResults.value = nearby
                } else {
                    // 결과가 부족하면 필터링 조건을 낮춰서 다시 시도
                    val moreResults = placesRepository.getNearbyPlaces(
                        lat = lat,
                        lng = lng,
                        radius = 5000, // 반경 확대
                        type = placeType
                    ).filter {
                        it.rating >= minRating - 0.5 && it.userRatingsTotal >= minReviews - 2
                    }
                    
                    if (moreResults.size >= minResultCount) {
                        _searchResults.value = moreResults
                    } else {
                        // 그래도 부족하면 필터링 없이 모든 결과 사용
                        val allResults = placesRepository.getNearbyPlaces(
                            lat = lat,
                            lng = lng,
                            radius = 8000, // 더 넓은 반경
                            type = placeType
                        )
                        
                        // 최소한 3개 이상 보장
                        _searchResults.value = allResults.take(
                            maxOf(minResultCount, allResults.size)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "주변 장소 검색 오류: ${e.message}")
                _errorMessage.value = "주변 장소를 검색하지 못했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 현재 위치 정보를 가져옵니다.
     * 위치 권한이 있어야 합니다.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                // Fused Location API 사용
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            val latLng = LatLng(it.latitude, it.longitude)
                            Log.d(TAG, "현재 위치: $latLng")
                            callback(latLng)
                        } ?: run {
                            // 위치를 가져오지 못한 경우 대체 방법 시도
                            getLastKnownLocation()?.let {
                                val latLng = LatLng(it.latitude, it.longitude)
                                Log.d(TAG, "마지막 알려진 위치: $latLng")
                                callback(latLng)
                            } ?: run {
                                Log.d(TAG, "위치를 가져올 수 없습니다")
                                callback(null)
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "위치 정보 가져오기 실패: ${e.message}")
                        callback(null)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "위치 정보 가져오기 오류: ${e.message}")
                callback(null)
            }
        } else {
            Log.d(TAG, "위치 권한이 없습니다")
            callback(null)
        }
    }
    
    /**
     * 마지막으로 알려진 위치를 가져옵니다 (대체 방법)
     */
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        
        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        
        return bestLocation
    }
    
    /**
     * 별자리와 도시 목록을 초기화 (홈 탭 클릭 시)
     */
    fun resetHomeState() {
        loadRecommendedPlaces()
        _searchResults.value = emptyList()
        _errorMessage.value = null
    }
} 