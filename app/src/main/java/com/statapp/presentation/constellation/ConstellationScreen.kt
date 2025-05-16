package com.statapp.presentation.constellation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.statapp.presentation.navigation.Screen
import com.statapp.ui.components.StarTripBottomNavigation
import com.statapp.ui.components.StarryBackground
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.drawscope.DrawScope

data class ConstellationData(
    val id: String,
    val name: String,
    val date: String,
    val description: String,
    val stars: List<Offset>,
    val lines: List<Pair<Int, Int>>
)

@Composable
fun ConstellationScreen(
    navController: NavController? = null
) {
    var selectedTabIndex by remember { mutableStateOf(2) } // Constellation tab selected
    var selectedCity by remember { mutableStateOf("Seoul, Korea") }
    var rotationAngle by remember { mutableStateOf(0f) }
    
    // 샘플 별자리 데이터
    val constellations = remember {
        listOf(
            ConstellationData(
                id = "seoul",
                name = "Seoul Adventure",
                date = "2023.12.20-2024.01.05",
                description = "A winter journey through Seoul's most iconic landmarks and hidden gems.",
                stars = listOf(
                    Offset(0.2f, 0.3f),
                    Offset(0.3f, 0.2f),
                    Offset(0.4f, 0.25f),
                    Offset(0.5f, 0.3f),
                    Offset(0.6f, 0.4f),
                    Offset(0.5f, 0.5f)
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
                id = "busan",
                name = "Busan Beaches",
                date = "2024.06.10-2024.06.17",
                description = "Exploring the coastal wonders of Busan, from Haeundae to Gwangalli.",
                stars = listOf(
                    Offset(0.3f, 0.6f),
                    Offset(0.4f, 0.5f),
                    Offset(0.5f, 0.6f),
                    Offset(0.6f, 0.7f),
                    Offset(0.7f, 0.6f)
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
                id = "jeju",
                name = "Jeju Island Exploration",
                date = "2023.08.01-2023.08.07",
                description = "A summer adventure around Jeju's volcanic landscapes and pristine beaches.",
                stars = listOf(
                    Offset(0.4f, 0.4f),
                    Offset(0.5f, 0.3f),
                    Offset(0.6f, 0.2f),
                    Offset(0.7f, 0.3f),
                    Offset(0.6f, 0.4f)
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
                                2 -> { /* 이미 Constellation 화면에 있음 */ }
                                3 -> it.navigate(Screen.Plan.route)
                                4 -> it.navigate(Screen.Profile.route)
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                // Title
                Text(
                    text = "Choose your",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "StarTrip.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // Globe icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, Color.White, CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Globe",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Constellation dial
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(300.dp)
                ) {
                    // Outer ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                    ) {
                        // Dial markings
                        for (i in 0 until 12) {
                            val angleRad = Math.toRadians((i * 30).toDouble())
                            val startX = 150 + 140 * cos(angleRad)
                            val startY = 150 + 140 * sin(angleRad)
                            val endX = 150 + 150 * cos(angleRad)
                            val endY = 150 + 150 * sin(angleRad)
                            
                            Box(
                                modifier = Modifier
                                    .size(1.dp, 10.dp)
                                    .offset(
                                        x = startX.dp - 0.5.dp,
                                        y = startY.dp - 5.dp
                                    )
                                    .rotate((i * 30).toFloat())
                                    .background(Color.Gray.copy(alpha = 0.7f))
                            )
                        }
                        
                        // Constellation patterns
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Create random star patterns
                            val starPoints = listOf(
                                // Constellation 1
                                listOf(
                                    Offset(100f, 80f),
                                    Offset(130f, 100f),
                                    Offset(160f, 90f)
                                ),
                                // Constellation 2
                                listOf(
                                    Offset(210f, 120f),
                                    Offset(240f, 150f),
                                    Offset(270f, 130f)
                                ),
                                // Constellation 3
                                listOf(
                                    Offset(80f, 180f),
                                    Offset(110f, 210f),
                                    Offset(140f, 190f)
                                ),
                                // Constellation 4
                                listOf(
                                    Offset(180f, 230f),
                                    Offset(210f, 260f),
                                    Offset(240f, 240f)
                                ),
                                // Constellation 5
                                listOf(
                                    Offset(60f, 260f),
                                    Offset(90f, 280f),
                                    Offset(120f, 250f)
                                )
                            )
                            
                            // Draw constellations
                            starPoints.forEach { constellation ->
                                // Draw lines between stars
                                for (i in 0 until constellation.size - 1) {
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.3f),
                                        start = constellation[i],
                                        end = constellation[i + 1],
                                        strokeWidth = 1f
                                    )
                                }
                                
                                // Draw stars
                                constellation.forEach { point ->
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.6f),
                                        radius = 2f,
                                        center = point
                                    )
                                }
                            }
                        }
                    }
                    
                    // Center element
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    ) {
                        // Constellation icon with glow
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFFFD700).copy(alpha = 0.1f),
                                            Color.Transparent
                                        ),
                                        radius = 100f
                                    )
                                )
                        )
                        
                        Card(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black
                            ),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFFDAA520))
                                ),
                                width = 2.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                // Constellation icon
                                Canvas(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.Center)
                                ) {
                                    val width = size.width
                                    val height = size.height
                                    val scale = minOf(width, height) / 50f
                                    
                                    // Draw constellation lines
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(10f * scale, 10f * scale),
                                        end = Offset(20f * scale, 15f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(20f * scale, 15f * scale),
                                        end = Offset(30f * scale, 10f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(30f * scale, 10f * scale),
                                        end = Offset(40f * scale, 20f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(15f * scale, 25f * scale),
                                        end = Offset(25f * scale, 30f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(25f * scale, 30f * scale),
                                        end = Offset(35f * scale, 25f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(20f * scale, 35f * scale),
                                        end = Offset(30f * scale, 40f * scale),
                                        strokeWidth = scale / 2
                                    )
                                    
                                    // Draw constellation points
                                    val points = listOf(
                                        Offset(10f * scale, 10f * scale),
                                        Offset(20f * scale, 15f * scale),
                                        Offset(30f * scale, 10f * scale),
                                        Offset(40f * scale, 20f * scale),
                                        Offset(15f * scale, 25f * scale),
                                        Offset(25f * scale, 30f * scale),
                                        Offset(35f * scale, 25f * scale),
                                        Offset(20f * scale, 35f * scale),
                                        Offset(30f * scale, 40f * scale)
                                    )
                                    
                                    points.forEach { point ->
                                        drawCircle(
                                            color = Color.White,
                                            radius = scale / 2,
                                            center = point
                                        )
                                    }
                                }
                            }
                        }
                        
                        // City name and date info
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 60.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = selectedCity,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "22.12-20.01",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ConstellationCard(
    constellation: ConstellationData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 별자리 시각화
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.DarkGray.copy(alpha = 0.4f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // 별자리 선 그리기
                    constellation.lines.forEach { (startIdx, endIdx) ->
                        if (startIdx < constellation.stars.size && endIdx < constellation.stars.size) {
                            val start = Offset(
                                constellation.stars[startIdx].x * size.width,
                                constellation.stars[startIdx].y * size.height
                            )
                            val end = Offset(
                                constellation.stars[endIdx].x * size.width,
                                constellation.stars[endIdx].y * size.height
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.7f),
                                start = start,
                                end = end,
                                strokeWidth = 2f
                            )
                        }
                    }
                    
                    // 별 그리기
                    constellation.stars.forEach { starPosition ->
                        val position = Offset(
                            starPosition.x * size.width,
                            starPosition.y * size.height
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4f,
                            center = position
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 별자리 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = constellation.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = constellation.date,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Text(
                    text = constellation.description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Details",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
} 