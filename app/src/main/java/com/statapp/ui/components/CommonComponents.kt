package com.statapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.statapp.R
import com.statapp.ui.theme.DarkBackground
import com.statapp.ui.theme.NavActive
import com.statapp.ui.theme.NavBackground
import com.statapp.ui.theme.NavInactive
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Starry background component used in multiple screens
 */
@Composable
fun StarryBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bg_starapp),
            contentDescription = "Starry Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Optional content overlay
        content()
    }
}

/**
 * Custom bottom navigation bar for the app
 */
@Composable
fun StarTripBottomNavigation(
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit
) {
    // 배경색 설정
    val backgroundColor = Color.Transparent
    
    // 모든 탭에 동일한 색상 스타일 적용
    val activeColor = Color.White
    val inactiveColor = Color.LightGray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 네비게이션 바 배경
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(28.dp)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(width = 300.dp, height = 64.dp)
        ) {
            // ub124ube44uac8cuc774uc158 uc544uc774ud15c ubc30uce58
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home ubc84ud2bc
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onIndexSelected(0) }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (selectedIndex == 0) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "HOME",
                        fontSize = 10.sp,
                        color = if (selectedIndex == 0) activeColor else inactiveColor,
                        fontWeight = if (selectedIndex == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
                
                // Search ubc84ud2bc
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onIndexSelected(1) }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (selectedIndex == 1) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "SEARCH",
                        fontSize = 10.sp,
                        color = if (selectedIndex == 1) activeColor else inactiveColor,
                        fontWeight = if (selectedIndex == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
                
                // uc911uc559 ube48 uacf5uac04 (uc9c0uad6cubcf8 uc544uc774ucf58uc744 uc704ud55c uacf5uac04)
                Spacer(modifier = Modifier.width(56.dp))
                
                // Plan ubc84ud2bc
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onIndexSelected(3) }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Plan",
                        tint = if (selectedIndex == 3) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "PLAN",
                        fontSize = 10.sp,
                        color = if (selectedIndex == 3) activeColor else inactiveColor,
                        fontWeight = if (selectedIndex == 3) FontWeight.Bold else FontWeight.Normal
                    )
                }
                
                // Profile ubc84ud2bc
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onIndexSelected(4) }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "My",
                        tint = if (selectedIndex == 4) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "MY",
                        fontSize = 10.sp,
                        color = if (selectedIndex == 4) activeColor else inactiveColor,
                        fontWeight = if (selectedIndex == 4) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        
        // 중앙 지구본 아이콘 (돌출된 형태)
        Box(
            modifier = Modifier
                .offset(y = (-36).dp)
                .size(56.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                .clickable { onIndexSelected(2) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Map",
                tint = if (selectedIndex == 2) activeColor else inactiveColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

/**
 * 2D 지구본 컴포넌트 - 3D 효과를 주는 2D 구현
 */
@Composable
fun RotatingGlobe(
    modifier: Modifier = Modifier,
    rotationY: Float = 0f
) {
    Box(
        modifier = modifier
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
    ) {
        // 지구본 베이스 (바다)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0277BD), // 밝은 바다색
                            Color(0xFF01579B), // 중간 바다색
                            Color(0xFF0D47A1)  // 어두운 바다색
                        )
                    )
                )
        )
        
        // 대륙 그리기
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            
            // 아시아/한국 (중앙 오른쪽)
            val asiaPath = Path().apply {
                moveTo(centerX + width * 0.1f, centerY - height * 0.1f)
                lineTo(centerX + width * 0.25f, centerY - height * 0.05f)
                lineTo(centerX + width * 0.3f, centerY + height * 0.1f)
                lineTo(centerX + width * 0.2f, centerY + height * 0.2f)
                lineTo(centerX + width * 0.05f, centerY + height * 0.15f)
                close()
            }
            
            // 유럽 (왼쪽 상단)
            val europePath = Path().apply {
                moveTo(centerX - width * 0.15f, centerY - height * 0.2f)
                lineTo(centerX - width * 0.05f, centerY - height * 0.15f)
                lineTo(centerX - width * 0.08f, centerY)
                lineTo(centerX - width * 0.2f, centerY + height * 0.05f)
                lineTo(centerX - width * 0.25f, centerY - height * 0.1f)
                close()
            }
            
            // 아프리카 (왼쪽 중앙-하단)
            val africaPath = Path().apply {
                moveTo(centerX - width * 0.1f, centerY)
                lineTo(centerX, centerY + height * 0.05f)
                lineTo(centerX - width * 0.05f, centerY + height * 0.2f)
                lineTo(centerX - width * 0.2f, centerY + height * 0.25f)
                lineTo(centerX - width * 0.25f, centerY + height * 0.1f)
                close()
            }
            
            // 북미 (왼쪽 상단)
            val northAmericaPath = Path().apply {
                moveTo(centerX - width * 0.3f, centerY - height * 0.25f)
                lineTo(centerX - width * 0.15f, centerY - height * 0.2f)
                lineTo(centerX - width * 0.2f, centerY - height * 0.05f)
                lineTo(centerX - width * 0.3f, centerY + height * 0.1f)
                lineTo(centerX - width * 0.35f, centerY - height * 0.1f)
                close()
        }
        
            // 남미 (왼쪽 하단)
            val southAmericaPath = Path().apply {
                moveTo(centerX - width * 0.25f, centerY + height * 0.1f)
                lineTo(centerX - width * 0.15f, centerY + height * 0.15f)
                lineTo(centerX - width * 0.2f, centerY + height * 0.3f)
                lineTo(centerX - width * 0.3f, centerY + height * 0.25f)
                close()
            }
            
            // 호주 (오른쪽 하단)
            val australiaPath = Path().apply {
                moveTo(centerX + width * 0.2f, centerY + height * 0.15f)
                lineTo(centerX + width * 0.3f, centerY + height * 0.1f)
                lineTo(centerX + width * 0.3f, centerY + height * 0.25f)
                lineTo(centerX + width * 0.15f, centerY + height * 0.3f)
                close()
                }
                
            // 대륙 그리기 - 회전 효과는 X 좌표를 조정하여 표현
            val rotationOffset = (rotationY / 360f) * width
            
            val offsetPaths = listOf(
                asiaPath, europePath, africaPath, 
                northAmericaPath, southAmericaPath, australiaPath
            )
            
            offsetPaths.forEach { path ->
                // 경도에 따른 밝기 변화 계산 (간단한 3D 효과)
                val brightness = 0.7f + 0.3f * (1 - Math.abs(rotationY / 180f))
                
                drawPath(
                    path = path,
                    color = Color(0xFF4CAF50).copy(alpha = brightness.toFloat()),
                    style = Stroke(width = 3f)
                )
                drawPath(
                    path = path,
                    color = Color(0xFF4CAF50).copy(alpha = (brightness * 0.5f).toFloat())
                )
            }
        }
        
        // 반사광 효과
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f)
                        ),
                        center = Offset(0.3f, 0.3f),
                        radius = 0.7f
                    )
                )
        )
    }
} 