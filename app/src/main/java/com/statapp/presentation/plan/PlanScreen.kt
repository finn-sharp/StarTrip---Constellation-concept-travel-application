package com.statapp.presentation.plan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.statapp.ui.components.StarTripBottomNavigation
import com.statapp.ui.theme.NavActive
import com.statapp.presentation.navigation.Screen
import com.statapp.ui.components.StarryBackground

@Composable
fun PlanScreen(
    navController: NavController? = null
) {
    var planStep by remember { mutableStateOf(1) }
    var selectedIndex by remember { mutableStateOf(3) }

    StarryBackground {
    Scaffold(
            containerColor = Color.Transparent,
        bottomBar = {
            StarTripBottomNavigation(
                selectedIndex = selectedIndex,
                onIndexSelected = { index ->
                    selectedIndex = index
                        navController?.let {
                            when (index) {
                                0 -> it.navigate(Screen.Home.route)
                                1 -> it.navigate(Screen.Search.route)
                                2 -> it.navigate(Screen.Constellation.route)
                                3 -> { /* uc774ubbf8 Plan ud654uba74uc5d0 uc788uc74c */ }
                                4 -> it.navigate(Screen.Profile.route)
                            }
                        }
                }
            )
        }
    ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.95f))
            ) {
        when (planStep) {
            1 -> PlanStep1(padding, onNextStep = { planStep = 2 })
            2 -> PlanStep2(padding, onNextStep = { planStep = 3 }, onPrevStep = { planStep = 1 })
            3 -> PlanStep3(padding, onPrevStep = { planStep = 2 })
                }
            }
        }
    }
}

@Composable
fun PlanStep1(
    padding: PaddingValues,
    onNextStep: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title with improved styling
        Text(
            text = "Create Your",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
            color = Color.Black,
        )
        
        Text(
            text = "Constellation",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "As you plan your journey, your own constellation begins to shine.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Globe icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Globe",
                    tint = Color.White
                )
            }
        }
        
        // Map visualization
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A237E).copy(alpha = 0.8f),
                            Color(0xFF000051).copy(alpha = 0.8f)
                        )
                    )
                )
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            // Map points
            ConstellationPoint(
                number = 1,
                modifier = Modifier
                    .offset(x = 100.dp, y = 50.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 2,
                modifier = Modifier
                    .offset(x = 150.dp, y = 80.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 3,
                modifier = Modifier
                    .offset(x = 60.dp, y = 100.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 4,
                modifier = Modifier
                    .offset(x = 200.dp, y = 120.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 5,
                modifier = Modifier
                    .offset(x = 120.dp, y = 150.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 6,
                modifier = Modifier
                    .offset(x = 220.dp, y = 40.dp),
                color = Color.White
            )
            
            ConstellationPoint(
                number = 7,
                modifier = Modifier
                    .offset(x = 240.dp, y = 90.dp),
                color = Color.White
            )
        }
        
        // Constellation icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ConstellationIcon(
                    modifier = Modifier.size(40.dp),
                    color = Color.White
                )
            }
        }
        
        // Course information
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.8f))
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Seoul basic course", color = Color.White)
                Text("5.56km ▼", color = Color.LightGray)
            }
        }
        
        // Location list
        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LocationItem(
                number = 1,
                title = "Seoul Central City Terminal",
                description = "Arrival to Seoul",
                isActive = true
            )
            
            LocationItem(
                number = 2,
                title = "Namsan Tower",
                description = "Tourist Hotspot",
                isActive = false
            )
            
            LocationItem(
                number = 3,
                title = "Hancock N",
                description = "Food & Cafe",
                isActive = false
            )
            
            LocationItem(
                number = 4,
                title = "Cinnabon",
                description = "Time: 20min",
                isActive = false
            )
        }
        
        // Create button
        Button(
            onClick = { onNextStep() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700).copy(alpha = 0.7f)
            )
        ) {
            Text(
                "Create your StarTrip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LocationItem(
    number: Int,
    title: String,
    description: String,
    isActive: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        // Number circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) Color.White else Color.Black.copy(alpha = 0.7f))
                .border(1.dp, Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = if (isActive) Color.Black else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Location details
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun ConstellationIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val scale = minOf(width, height) / 50f
        
        // Draw constellation lines
        drawLine(
            color = color,
            start = Offset(10f * scale, 10f * scale),
            end = Offset(20f * scale, 15f * scale),
            strokeWidth = scale
        )
        drawLine(
            color = color,
            start = Offset(20f * scale, 15f * scale),
            end = Offset(30f * scale, 10f * scale),
            strokeWidth = scale
        )
        drawLine(
            color = color,
            start = Offset(30f * scale, 10f * scale),
            end = Offset(40f * scale, 20f * scale),
            strokeWidth = scale
        )
        drawLine(
            color = color,
            start = Offset(15f * scale, 25f * scale),
            end = Offset(25f * scale, 30f * scale),
            strokeWidth = scale
        )
        drawLine(
            color = color,
            start = Offset(25f * scale, 30f * scale),
            end = Offset(35f * scale, 25f * scale),
            strokeWidth = scale
        )
        drawLine(
            color = color,
            start = Offset(20f * scale, 35f * scale),
            end = Offset(30f * scale, 40f * scale),
            strokeWidth = scale
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
                color = color,
                radius = scale,
                center = point
            )
        }
    }
}

@Composable
fun PlanStep2(
    padding: PaddingValues,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onPrevStep() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Set Destination",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Search field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Where do you want to go?", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.8f),
                    focusedContainerColor = Color.Black.copy(alpha = 0.8f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                ),
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
            )
        }
        
        // Trending places
        Text(
            text = "Trending Places",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Place grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            item { PlaceChip("Seoul", isSelected = true) }
            item { PlaceChip("Busan") }
            item { PlaceChip("Jeju") }
            item { PlaceChip("Jeonju") }
            item { PlaceChip("Yangyang") }
            item { PlaceChip("Damyang") }
            item { PlaceChip("Daegu", isDisabled = true) }
            item { PlaceChip("Gangneung", isDisabled = true) }
            item { PlaceChip("Gwangju", isDisabled = true) }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Calendar (dummy)
        SettingItem(title = "Date", value = "6/26/Mon")
        
        // Travelers
        SettingItem(title = "Travelers", value = "2")
        
        // Add Course
        SettingItem(title = "Add Course")
        
        // Create button
        Button(
            onClick = { onNextStep() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700).copy(alpha = 0.7f)
            )
        ) {
            Text(
                "Create your StarTrip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PlanStep3(
    padding: PaddingValues,
    onPrevStep: () -> Unit
) {
    var searchText by remember { mutableStateOf("Hampyeong") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onPrevStep() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Set Destination",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Search field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Where do you want to go?", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.8f),
                    focusedContainerColor = Color.Black.copy(alpha = 0.8f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                ),
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
            )
        }
        
        // Trending places
        Text(
            text = "Trending Places",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Place selection
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            item { PlaceChip("Seoul", isActive = true) }
            item { PlaceChip("Busan") }
            item { PlaceChip("Jeju") }
            item { PlaceChip("Jeonju") }
            item { PlaceChip("Yangyang") }
            item { PlaceChip("Damyang") }
            item { PlaceChip("Daegu", isDisabled = true) }
            item { PlaceChip("Gangneung", isDisabled = true) }
            item { PlaceChip("Gwangju", isDisabled = true) }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Date and Travelers
        SettingItem(title = "Date", value = "6/26/Mon")
        SettingItem(title = "Travelers", value = "2")
        
        // Courses
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Course",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "More",
                        tint = Color.LightGray
                    )
                }
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CourseItem(number = 1, title = "Hampyeong Public Terminal")
                    CourseItem(number = 2, title = "Hampyeong Dolmeon Beach")
                    CourseItem(number = 3, title = "Po-oh coffee")
                    CourseItem(number = 4, title = "")
                }
            }
        }
        
        // Create button
        Button(
            onClick = { /* Finish */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700).copy(alpha = 0.7f)
            )
        ) {
            Text(
                "Create your StarTrip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ConstellationPoint(
    number: Int,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2196F3)
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlaceChip(
    name: String,
    isSelected: Boolean = false,
    isActive: Boolean = false,
    isDisabled: Boolean = false
) {
    val backgroundColor = when {
        isSelected -> Color(0xFF3F51B5).copy(alpha = 0.7f)
        isActive -> Color(0xFF006064).copy(alpha = 0.7f)
        isDisabled -> Color.DarkGray.copy(alpha = 0.3f)
        else -> Color.Black.copy(alpha = 0.4f)
    }
    
    val borderColor = when {
        isSelected -> Color(0xFF5C6BC0)
        isActive -> Color(0xFF00ACC1)
        else -> Color.Transparent
    }
    
    val textColor = when {
        isDisabled -> Color.Gray
        else -> Color.White
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected || isActive) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            color = textColor
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    value: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White
            )
            
            if (value != null) {
                Text(
                    text = "$value ▼",
                    color = Color.LightGray
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "More",
                    tint = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun CourseItem(
    number: Int,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Blue.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = title,
                fontSize = 14.sp,
                color = if (title.isEmpty()) Color.Gray else Color.White
            )
        }
        
        Icon(
            Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.LightGray,
            modifier = Modifier.size(18.dp)
        )
    }
} 