package com.statapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.statapp.presentation.navigation.Screen
import com.statapp.ui.components.StarTripBottomNavigation
import com.statapp.ui.components.StarryBackground
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.drawscope.Stroke
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    navController: NavController? = null
) {
    var selectedTabIndex by remember { mutableStateOf(4) } // Profile tab selected
    
    // 샘플 별자리 데이터
    val constellations = remember {
        listOf(
            ConstellationData(
                name = "Busan, Korea",
                date = "23.12-20.01",
                path = listOf(
                    Offset(10f, 30f),
                    Offset(20f, 20f),
                    Offset(30f, 25f),
                    Offset(25f, 40f),
                    Offset(15f, 35f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            ),
            ConstellationData(
                name = "Seoul, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(10f, 15f),
                    Offset(20f, 5f),
                    Offset(30f, 10f),
                    Offset(35f, 20f),
                    Offset(25f, 30f),
                    Offset(15f, 25f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 5),
                    Pair(5, 0)
                )
            ),
            ConstellationData(
                name = "Jeonju, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(20f, 10f),
                    Offset(30f, 15f),
                    Offset(25f, 25f),
                    Offset(15f, 30f),
                    Offset(10f, 20f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            ),
            ConstellationData(
                name = "Jeju, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(15f, 15f),
                    Offset(25f, 10f),
                    Offset(35f, 20f),
                    Offset(30f, 30f),
                    Offset(20f, 25f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            )
        )
    }
    
    val sharedConstellations = remember {
        listOf(
            ConstellationData(
                name = "Jeju, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(15f, 15f),
                    Offset(25f, 10f),
                    Offset(35f, 20f),
                    Offset(30f, 30f),
                    Offset(20f, 25f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            ),
            ConstellationData(
                name = "Jeonju, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(10f, 20f),
                    Offset(20f, 10f),
                    Offset(30f, 15f),
                    Offset(25f, 25f),
                    Offset(15f, 30f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            ),
            ConstellationData(
                name = "Busan, Korea",
                date = "23.12-20.01",
                path = listOf(
                    Offset(10f, 30f),
                    Offset(20f, 20f),
                    Offset(30f, 25f),
                    Offset(25f, 40f),
                    Offset(15f, 35f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 0)
                )
            ),
            ConstellationData(
                name = "Seoul, Korea",
                date = "22.12-20.01",
                path = listOf(
                    Offset(10f, 15f),
                    Offset(20f, 5f),
                    Offset(30f, 10f),
                    Offset(35f, 20f),
                    Offset(25f, 30f),
                    Offset(15f, 25f)
                ),
                lines = listOf(
                    Pair(0, 1),
                    Pair(1, 2),
                    Pair(2, 3),
                    Pair(3, 4),
                    Pair(4, 5),
                    Pair(5, 0)
                )
            )
        )
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
                                0 -> it.navigate(Screen.Home.route)
                                1 -> it.navigate(Screen.Search.route)
                                2 -> it.navigate(Screen.Constellation.route)
                                3 -> it.navigate(Screen.Plan.route)
                                4 -> { /* uc774ubbf8 ud504ub85cud544 ud654uba74uc5d0 uc788uc74c */ }
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    // 헤더 여백
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 프로필 섹션 헤더
                    Text(
                        text = "My profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // 프로필 정보 카드 - 디자인 개선
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.8f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 프로필 이미지
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            ) {
                                AsyncImage(
                                    model = "https://i.imgur.com/DvpvklR.png",
                                    contentDescription = "Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // 프로필 정보
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Jennie Kim",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Introduce my Journey on Foot 👣",
                                    fontSize = 14.sp,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // 편집 버튼
                            Button(
                                onClick = { /* 프로필 편집 */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(50)
                                )
                            ) {
                                Text(
                                    text = "Edit Profile",
                                    fontSize = 12.sp
                                )
                        }
                    }
                }

                // My Constellation 섹션
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                            .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "My constellation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(120.dp)
                    ) {
                        items(constellations) { constellation ->
                            ConstellationItem(constellation = constellation)
                        }
                    }
                }

                // Shared Constellation 섹션
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                            .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "My shared constellation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(120.dp)
                    ) {
                        items(sharedConstellations) { constellation ->
                            ConstellationItem(constellation = constellation)
                        }
                    }
                }

                // Settings 섹션
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                            .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                        // 설정 항목들
                            val settingsItems = listOf(
                                "Privacy",
                                "Login & Security",
                                "Accessibility"
                            )
                        settingsItems.forEach { setting ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.8f)
                                )
                            ) {
                                SettingsItem(
                                    title = setting,
                                    onClick = { /* 설정 항목 클릭 */ }
                                )
                        }
                    }
                }

                // 하단 여백
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ConstellationItem(constellation: ConstellationData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(2.dp)
    ) {
        // 별자리 박스
        Box(
            modifier = Modifier
                .size(60.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color.Black)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 별자리 선 그리기
                for (line in constellation.lines) {
                    if (line.first < constellation.path.size && line.second < constellation.path.size) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.6f),
                            start = constellation.path[line.first],
                            end = constellation.path[line.second],
                            strokeWidth = 1.5f
                        )
                    }
                }

                // 별 그리기
                for (point in constellation.path) {
                    drawCircle(
                        color = Color.White,
                        radius = 1.5f,
                        center = point
                    )
                }
            }
        }

        // 별자리 이름과 날짜
        Text(
            text = constellation.name,
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = constellation.date,
            color = Color.DarkGray,
            fontSize = 8.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
    }
}

data class ConstellationData(
    val name: String,
    val date: String,
    val path: List<Offset>,
    val lines: List<Pair<Int, Int>>
) 