package com.statapp.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.statapp.R
import com.statapp.domain.model.PlaceSummary
import com.statapp.presentation.home.HomeViewModel
import com.statapp.presentation.navigation.Screen
import com.statapp.ui.components.StarTripBottomNavigation
import com.statapp.ui.components.StarryBackground
import kotlinx.coroutines.delay
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onPlaceSelected: ((PlaceSummary) -> Unit)? = null,
    navController: NavController? = null
) {
    var selectedTabIndex by remember { mutableStateOf(1) } // Search tab selected
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    
    // ViewModel에서 데이터 가져오기
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    // 현재 위치를 저장할 상태
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    
    // 페이지 진입 시 위치 권한 요청 및 현재 위치 기반으로 주변 맛집 로드
    LaunchedEffect(Unit) {
        viewModel.getCurrentLocation { location ->
            userLocation = location
            location?.let {
                // 현재 위치 주변의 맛집 로드 (별점 4.0 이상, 리뷰 5개 이상)
                // 최소 3개 이상의 결과를 보장하기 위해 필터링 조건 조정
                viewModel.loadNearbyRestaurantsWithMinResults(it.latitude, it.longitude, 4.0, 5, 3)
            }
        }
    }
    
    // 검색어 타이핑 후 자동 검색 구현
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            isSearchActive = false
            // 검색어가 비었을 때 다시 주변 맛집 표시
            userLocation?.let {
                // 최소 3개 이상의 결과를 보장
                viewModel.loadNearbyRestaurantsWithMinResults(it.latitude, it.longitude, 4.0, 5, 3)
            }
        }
        // 자동 검색 기능 제거 - 엔터키나 검색 버튼 클릭으로만 검색하도록 함
    }
    
    // Categories (tabs)
    val categories = listOf(
        "Trending Course", 
        "Top Picks", 
        "Viral Course", 
        "No-Car Needed Course",
        "Hot"
        )
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    
    // 카테고리 선택 시 새로운 검색 실행
    LaunchedEffect(selectedCategory) {
        if (searchQuery.isEmpty() && userLocation != null) {
            // 검색어가 없는 경우 카테고리에 따라 다른 주변 장소 검색
            userLocation?.let {
                when (selectedCategory) {
                    "Restaurants" -> viewModel.loadNearbyRestaurantsWithMinResults(it.latitude, it.longitude, 4.0, 5, 3)
                    else -> viewModel.searchNearbyPlacesWithMinResults(it.latitude, it.longitude, selectedCategory, 4.0, 5, 3)
                }
            }
        } else if (searchQuery.isNotEmpty()) {
            // 검색어가 있는 경우 검색어와 카테고리 조합으로 검색
            // 0.5초 딜레이 후 검색 (사용자 입력 완료 기다림)
            delay(500)
            viewModel.searchPlaces("$searchQuery ${selectedCategory.lowercase()}")
        }
    }

    // StarryBackground 사용
    StarryBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                StarTripBottomNavigation(
                    selectedIndex = selectedTabIndex,
                    onIndexSelected = { index -> 
                        selectedTabIndex = index
                        navController?.let {
                            when (index) {
                                0 -> it.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                                1 -> { /* 이미 검색 화면에 있음 */ }
                                2 -> it.navigate(Screen.Constellation.route)
                                3 -> it.navigate(Screen.Plan.route)
                                4 -> it.navigate(Screen.Profile.route) // 프로필 화면으로 이동
                            }
                        }
                    }
                )
            }
        ) { padding ->
            // 전체 화면에 반투명한 하양 배경 추가
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Search header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // 화면 상단 제목
                    Text(
                        text = "Find Places",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                        // Search bar - 검색 바 디자인 개선
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                                placeholder = { Text("Where do you want to go?", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50)),
                            colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            leadingIcon = {
                                IconButton(onClick = onNavigateBack) {
                                    Icon(
                                        Icons.Default.ChevronLeft,
                                        contentDescription = "Back",
                                        tint = Color.Black
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (searchQuery.isNotEmpty()) {
                                            isSearchActive = true
                                            viewModel.searchPlaces(searchQuery)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchQuery.isNotEmpty()) {
                                        isSearchActive = true
                                        viewModel.searchPlaces(searchQuery)
                                    }
                                }
                            ),
                            singleLine = true
                        )
                    }
                    
                        // Categories - 카테고리 디자인 개선
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (selectedCategory == category) 
                                            Color.Black
                                        else 
                                                Color.LightGray.copy(alpha = 0.4f)
                                    )
                                    .clickable { 
                                        selectedCategory = category
                                    }
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    color = if (selectedCategory == category) 
                                        Color.White
                                    else 
                                        Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                // 검색 결과 또는 추천 목록
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // 로딩 표시기
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                                color = Color.DarkGray
                        )
                    }
                    
                    // 검색 결과 없음 메시지
                    else if (searchResults.isEmpty() && isSearchActive) {
                        Text(
                            text = "No places found for \"$searchQuery\"",
                                color = Color.DarkGray,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                    
                    // 오류 메시지
                    else if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                    
                    // 검색 결과 목록
                    else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = if (isSearchActive) 
                                        "Search Results" 
                                    else if (selectedCategory == "Restaurants")
                                            "Trending Course"
                                    else
                                            "Popular ${selectedCategory}",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            
                            // 결과가 없을 때 처리
                            if (searchResults.isEmpty() && !isLoading) {
                                item {
                                    Text(
                                        text = "No results found. Try a different search or category.",
                                            color = Color.DarkGray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(vertical = 32.dp)
                                    )
                                }
                            }
                            
                            items(searchResults) { place ->
                                PlaceCard(
                                    place = place,
                                    onClick = {
                                        onPlaceSelected?.invoke(place)
                                    }
                                )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: PlaceSummary,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.6f)
        )
    ) {
        Column {
            // Image section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Place image
                if (place.photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(place.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = painterResource(id = R.drawable.bg_starapp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.bg_starapp),
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // Trending badge
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                        .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                        ) {
                            Text(
                        text = "Trending Course",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                }
                
                // Arrow icon
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = place.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                
                    Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                            contentDescription = "Rating",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                            text = if (place.userRatingsTotal > 1000) 
                                "${(place.userRatingsTotal / 1000)}K" 
                            else 
                                "${place.userRatingsTotal}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                }
                
                // Description
                Text(
                    text = "${place.types.firstOrNull()?.replace("_", " ")?.capitalize() ?: "Place"} Hot Spot Itinerary",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// String capitalize 확장 함수
private fun String.capitalize(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
} 