package com.statapp.presentation.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.statapp.R
import com.statapp.ui.components.StarryBackground

private const val TAG = "LoginScreen"

/**
 * 로그인 화면 UI입니다.
 * Google 로그인 버튼과 로그인 상태를 표시합니다.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasNavigated by remember { mutableStateOf(false) }
    
    // 앱 시작 시 이미 인증된 상태인지 확인
    LaunchedEffect(Unit) {
        Log.d(TAG, "LoginScreen 시작 - 초기 인증 상태: $isAuthenticated")
        if (isAuthenticated) {
            Log.d(TAG, "이미 인증된 상태로 앱 시작 - 홈 화면으로 즉시 이동")
            hasNavigated = true
            onLoginSuccess()
        }
    }
    
    // Google 로그인 결과 처리 런처
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Google 로그인 결과 받음: RESULT_OK")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "Google 계정 정보 얻음: ${account.email}")
                viewModel.signInWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google 로그인 실패: ${e.statusCode} - ${e.message}", e)
                errorMessage = "Google 로그인 실패: ${e.statusCode} - ${e.message}"
            }
        } else {
            Log.w(TAG, "Google 로그인 취소됨: resultCode = ${result.resultCode}")
            errorMessage = "로그인이 취소되었습니다."
        }
    }
    
    // 인증 상태 변경 감지 - LoginState와 조화롭게 동작하도록 조정
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated && !hasNavigated && loginState !is LoginState.Loading) {
            Log.d(TAG, "인증 상태 변경 감지: $isAuthenticated - 화면 전환")
            hasNavigated = true
            onLoginSuccess()
        }
    }
    
    // 로그인 상태에 따른 처리
    LaunchedEffect(loginState) {
        Log.d(TAG, "로그인 상태 변경: $loginState")
        when (loginState) {
            is LoginState.Success -> {
                Log.d(TAG, "로그인 성공: ${(loginState as LoginState.Success).user.email}")
                if (!hasNavigated) {
                    Log.d(TAG, "LoginState.Success로 화면 전환")
                    hasNavigated = true
                    onLoginSuccess()
                }
            }
            is LoginState.Error -> {
                Log.e(TAG, "로그인 에러: ${(loginState as LoginState.Error).message}")
                errorMessage = (loginState as LoginState.Error).message
                // 에러 발생 시 네비게이션 상태 초기화
                hasNavigated = false
            }
            is LoginState.Loading -> {
                Log.d(TAG, "로그인 로딩 중...")
                // 로딩 중에는 네비게이션 방지
                hasNavigated = false
            }
            else -> {
                Log.d(TAG, "초기 상태")
                // 초기 상태에서도 네비게이션 상태 초기화
                hasNavigated = false
            }
        }
    }
    
    StarryBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Title
            Text(
                text = "StarTrip",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 180.dp)
            )
            
            // Login section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Start with Google",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    OutlinedButton(
                        onClick = {
                            // Google 로그인 인텐트 시작
                            Log.d(TAG, "로그인 버튼 클릭")
                            val signInIntent = viewModel.getGoogleSignInIntent()
                            launcher.launch(signInIntent)
                        },
                        enabled = loginState !is LoginState.Loading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Logo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Text(
                            text = "Google로 로그인",
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
        
        // 로딩 인디케이터
        if (loginState is LoginState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        // 에러 메시지
        errorMessage?.let { message ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    Button(onClick = { errorMessage = null }) {
                        Text("확인")
                    }
                }
            ) {
                Text(message)
            }
        }
    }
} 