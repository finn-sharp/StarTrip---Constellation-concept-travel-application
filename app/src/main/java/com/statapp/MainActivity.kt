package com.statapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.statapp.presentation.navigation.NavHost
import com.statapp.ui.theme.StatAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 앱의 메인 엔트리 포인트입니다.
 * Jetpack Compose를 사용하여 UI를 구성하고 네비게이션을 관리합니다.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StatAppTheme {
                AppContent()
            }
        }
    }
}

/**
 * 앱의 메인 컴포저블 컨텐츠입니다.
 * 네비게이션 컨트롤러를 설정하고 앱의 UI 구조를 정의합니다.
 */
@Composable
fun AppContent() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}