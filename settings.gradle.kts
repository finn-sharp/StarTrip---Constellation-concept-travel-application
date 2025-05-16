pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("libs") {
            // Android 애플리케이션 플러그인
            plugin("android.application", "com.android.application").version("8.1.1")
            // Kotlin 안드로이드 플러그인
            plugin("kotlin.android", "org.jetbrains.kotlin.android").version("1.9.0")
            // Kotlin Compose 플러그인
            plugin("kotlin.compose", "org.jetbrains.kotlin.plugin.compose").version("1.5.1")
            
            // AndroidX 라이브러리
            library("androidx.core.ktx", "androidx.core:core-ktx:1.12.0")
            library("androidx.lifecycle.runtime.ktx", "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
            library("androidx.activity.compose", "androidx.activity:activity-compose:1.8.0")
            
            // Compose BOM
            library("androidx.compose.bom", "androidx.compose:compose-bom:2023.10.01")
            
            // Compose 라이브러리
            library("androidx.ui", "androidx.compose.ui:ui:1.5.4")
            library("androidx.ui.graphics", "androidx.compose.ui:ui-graphics:1.5.4")
            library("androidx.ui.tooling.preview", "androidx.compose.ui:ui-tooling-preview:1.5.4")
            library("androidx.material3", "androidx.compose.material3:material3:1.1.2")
            library("androidx.ui.tooling", "androidx.compose.ui:ui-tooling:1.5.4")
            library("androidx.ui.test.manifest", "androidx.compose.ui:ui-test-manifest:1.5.4")
            library("androidx.ui.test.junit4", "androidx.compose.ui:ui-test-junit4:1.5.4")
            
            // 테스트 라이브러리
            library("junit", "junit:junit:4.13.2")
            library("androidx.junit", "androidx.test.ext:junit:1.1.5")
            library("androidx.espresso.core", "androidx.test.espresso:espresso-core:3.5.1")
        }
    }
}

rootProject.name = "StatApp"
include(":app") 