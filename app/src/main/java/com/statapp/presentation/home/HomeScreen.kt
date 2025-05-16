package com.statapp.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.statapp.domain.model.CityData
import com.statapp.domain.model.RecommendedPlace
import com.statapp.presentation.navigation.Screen
import com.statapp.ui.components.RotatingGlobe
import com.statapp.ui.components.StarTripBottomNavigation
import com.statapp.ui.components.StarryBackground
import com.statapp.ui.theme.Cyan400
import kotlinx.coroutines.delay

/**
 * 홈 화면 UI입니다.
 * 위 부분에는 Gemini 추천 장소들을 별자리로 표현하고
 * 아래 부분에는 3D 지구본이 표시되며 확대/축소가 가능합니다.
 * 확대 시 Google Maps로 자연스럽게 전환됩니다.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    navController: NavController? = null
) {
    val TAG = "HomeScreen"
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    // 3D/2D 모드 상태
    var is3DMode by remember { mutableStateOf(true) }
    
    // 확대/축소 상태
    var scale by remember { mutableStateOf(1f) }
    
    // 지구본 회전 상태
    var rotationY by remember { mutableStateOf(0f) }
    
    // 애니메이션 스케일 값
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(300),
        label = "scale_animation"
    )
    
    // 2D 맵 알파값 (부드러운 전환을 위해)
    val mapAlpha by animateFloatAsState(
        targetValue = if (is3DMode) 0f else 1f,
        animationSpec = tween(500),
        label = "map_alpha"
    )
    
    // 3D 지구본 알파값
    val globeAlpha by animateFloatAsState(
        targetValue = if (is3DMode) 1f else 0f,
        animationSpec = tween(500),
        label = "globe_alpha"
    )
    
    // 위치 권한 상태
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // 현재 사용자 위치
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    
    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            // 위치 권한이 부여되면 현재 위치 정보 로드
            viewModel.getCurrentLocation { location ->
                userLocation = location
                // 현재 위치 주변의 장소 로드
                location?.let {
                    viewModel.loadNearbyPlaces(it.latitude, it.longitude)
                }
            }
        }
    }
    
    // 권한 확인 및 요청
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // 이미 권한이 있는 경우 위치 정보 로드
            viewModel.getCurrentLocation { location ->
                userLocation = location
                // 현재 위치 주변의 장소 로드
                location?.let {
                    viewModel.loadNearbyPlaces(it.latitude, it.longitude)
                }
            }
        }
    }
    
    // ViewModel에서 데이터 가져오기
    val recommendedPlaces by viewModel.recommendedPlaces.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    // 글로브 회전 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "globe_rotation")
    val autoRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "globe_rotation"
    )
    
    // 별 반짝임 애니메이션
    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_twinkle"
    )
    
    // 추천 장소 표시 애니메이션
    var showRecommendationInfo by remember { mutableStateOf<RecommendedPlace?>(null) }
    
    // 구글 맵 설정
    val defaultLocation = LatLng(37.566535, 126.9779692) // 서울 기본 위치
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation ?: defaultLocation, 10f
        )
    }
    
    // 맵 UI 설정
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = true,
            compassEnabled = true,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            rotationGesturesEnabled = true
        )
    }
    
    // 맵 속성 설정
    val mapProperties = remember {
        MapProperties(
            isMyLocationEnabled = hasLocationPermission,
            mapType = MapType.NORMAL,
            isTrafficEnabled = true
        )
    }
    
    // 홈 탭 클릭 시 상태 초기화
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 0) {
            // 홈 탭을 눌렀을 때 상태 초기화
            is3DMode = true
            scale = 1f
            viewModel.resetHomeState()
        }
    }
    
    // 사용자 위치가 업데이트되면 카메라 위치 업데이트
    LaunchedEffect(userLocation) {
        userLocation?.let {
            if (!is3DMode) {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngZoom(it, 12f)
                )
            }
        }
    }
    
    StarryBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                StarTripBottomNavigation(
                    selectedIndex = selectedTabIndex,
                    onIndexSelected = { index ->
                        selectedTabIndex = index
                        when (index) {
                            0 -> {
                                // 이미 홈 화면이므로 상태 초기화만
                                viewModel.resetHomeState()
                                is3DMode = true
                                scale = 1f
                            }
                            1 -> navController?.navigate(Screen.Search.route)
                            2 -> navController?.navigate(Screen.Constellation.route)
                            3 -> navController?.navigate(Screen.Plan.route)
                            4 -> navController?.navigate(Screen.Profile.route)
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // 상태바 제거 (시간 및 상태 아이콘 제거)
                
                // 콘텐츠 영역
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Gemini 추천 영역 (상단)
                    Box(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Gemini Recommended Places",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.TopCenter)
                        )
                        
                        // 로딩 중 표시
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }
                        
                        // 오류 메시지 표시
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        }
                        
                        // 추천 장소 별자리 표시 - 항상 표시되도록 수정
                        recommendedPlaces.forEach { place ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(
                                        x = (place.x * 300).dp,
                                        y = (place.y * 100).dp
                                    )
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, _, _, _ ->
                                            showRecommendationInfo = place
                                        }
                                    }
                            ) {
                                // 별자리 캔버스로 그리기
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    // 별 사이 선 그리기
                                    place.lines.forEach { (startIdx, endIdx) ->
                                        if (startIdx < place.stars.size && endIdx < place.stars.size) {
                                            drawLine(
                                                color = Color.White.copy(alpha = 0.6f),
                                                start = place.stars[startIdx],
                                                end = place.stars[endIdx],
                                                strokeWidth = 1.5f
                                            )
                                        }
                                    }
                                    
                                    // 별 그리기
                                    place.stars.forEach { position ->
                                        drawCircle(
                                            color = Color.White.copy(alpha = starAlpha),
                                            radius = 4f,
                                            center = position
                                        )
                                    }
                                }
                            }
                        }
                        
                        // 선택된 추천 장소 정보 표시
                        if (showRecommendationInfo != null) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(horizontal = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black.copy(alpha = 0.7f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.Yellow
                                            )
                                            Text(
                                                text = showRecommendationInfo!!.name,
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = showRecommendationInfo!!.description,
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            
                            // 일정 시간 후 정보 창 닫기
                            LaunchedEffect(showRecommendationInfo) {
                                delay(5000)
                                showRecommendationInfo = null
                            }
                        }
                    }
                    
                    // 지구본/지도 영역 (하단)
                    Box(
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        // 3D 글로브 모드
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(globeAlpha)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(280.dp)  // 크기를 200dp에서 280dp로 증가
                                    .align(Alignment.Center),
                            ) {
                                // 새로운 2D 지구본 컴포넌트 사용
                                RotatingGlobe(
                                    modifier = Modifier.fillMaxSize(),
                                    rotationY = rotationY
                                )
                                
                                // 자동 회전 애니메이션
                                LaunchedEffect(Unit) {
                                    while(true) {
                                        delay(50)
                                        rotationY += 0.5f
                                        if (rotationY > 360f) rotationY = 0f
                                    }
                                }
                                
                                // 줌 제스처 처리
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .pointerInput(Unit) {
                                            detectTransformGestures { _, pan, zoom, _ ->
                                                // 줌 인/아웃 처리
                                                scale *= zoom
                                                
                                                // 회전 처리
                                                rotationY += pan.x * 0.5f
                                                
                                                // 줌 범위 제한
                                                scale = scale.coerceIn(0.5f, 2.0f)
                                                
                                                // 일정 임계값에 도달하면 모드 전환
                                                if (scale > 1.5f && is3DMode) {
                                                    is3DMode = false
                                                } else if (scale < 0.8f && !is3DMode) {
                                                    is3DMode = true
                                                }
                                            }
                                        }
                                )
                            }
                        }
                        
                        // 2D 지도 모드 (Google Maps)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(if (is3DMode) 16.dp else 0.dp)
                                .clip(if (is3DMode) RoundedCornerShape(16.dp) else RoundedCornerShape(0.dp))
                                .alpha(mapAlpha)
                        ) {
                            // Google 지도 통합
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                properties = mapProperties,
                                uiSettings = mapUiSettings,
                                onMapLoaded = {
                                    Log.d(TAG, "Google Map loaded")
                                }
                            ) {
                                // 추천 장소들을 마커로 표시
                                recommendedPlaces.forEach { place ->
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(place.lat, place.lng)
                                        ),
                                        title = place.name,
                                        snippet = place.description
                                    )
                                }
                                
                                // 표시할 도시들을 마커로 표시
                                cities.forEach { city ->
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(city.lat, city.lng)
                                        ),
                                        title = city.name
                                    )
                                }
                                
                                // 사용자 현재 위치 표시
                                userLocation?.let { location ->
                                    Marker(
                                        state = MarkerState(position = location),
                                        title = "My Location",
                                        icon = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_BLUE
                                        )
                                    )
                                }
                            }
                            
                            if (!is3DMode) {
                                Text(
                                    text = "Pinch to zoom out for 3D globe",
                                    color = Color.Black.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 16.dp)
                                        .background(Color.White.copy(alpha = 0.7f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                        .clip(RoundedCornerShape(50))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 