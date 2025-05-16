package com.statapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.statapp.presentation.auth.LoginScreen
import com.statapp.presentation.auth.LoginViewModel
import com.statapp.presentation.home.HomeScreen
import com.statapp.presentation.home.HomeViewModel
import com.statapp.presentation.profile.ProfileScreen
import com.statapp.presentation.search.SearchScreen
import com.statapp.presentation.plan.PlanScreen
import com.statapp.presentation.constellation.ConstellationScreen

/**
 * 앱의 메인 네비게이션 그래프입니다.
 */
@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            Log.d("Navigation", "로그인 화면 진입")
            val viewModel: LoginViewModel = hiltViewModel()
            val isAuthenticated by viewModel.isAuthenticated.collectAsState()
            
            // 이미 로그인되어 있으면 홈으로 이동
            LaunchedEffect(isAuthenticated) {
                if (isAuthenticated) {
                    Log.d("Navigation", "이미 로그인되어 있음 - 홈 화면으로 이동")
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            Log.d("Navigation", "홈 화면 진입")
            val viewModel: HomeViewModel = hiltViewModel()
            
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }
        
        composable(Screen.Search.route) {
            Log.d("Navigation", "검색 화면 진입")
            SearchScreen(
                onNavigateBack = {
                    Log.d("Navigation", "검색 화면에서 뒤로 가기")
                    navController.navigateUp()
                },
                navController = navController
            )
        }
        
        composable(Screen.Profile.route) {
            Log.d("Navigation", "프로필 화면 진입")
            ProfileScreen(
                navController = navController
            )
        }
        
        composable(Screen.Plan.route) {
            Log.d("Navigation", "플랜 화면 진입")
            PlanScreen(
                navController = navController
            )
        }
        
        composable(Screen.Constellation.route) {
            Log.d("Navigation", "별자리 화면 진입")
            ConstellationScreen(
                navController = navController
            )
        }
    }
}

/**
 * 앱의 스크린 정의입니다.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Search : Screen("search")
    object Constellation : Screen("constellation")
    object Profile : Screen("profile")
    object Plan : Screen("plan")
} 