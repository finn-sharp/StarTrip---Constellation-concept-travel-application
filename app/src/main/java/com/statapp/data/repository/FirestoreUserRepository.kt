package com.statapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.statapp.domain.model.UserData
import com.statapp.domain.model.UserActivity
import com.statapp.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firestore를 사용한 사용자 데이터 리포지토리 구현
 */
class FirestoreUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val TAG = "FirestoreUserRepository"
    private val USERS_COLLECTION = "users"
    private val ACTIVITIES_COLLECTION = "activities"

    override fun getUserData(userId: String): Flow<UserData?> = callbackFlow {
        Log.d(TAG, "사용자 데이터 구독 시작: $userId")
        
        val listener = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 데이터 조회 오류: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }
                
                val userData = snapshot?.toObject(UserData::class.java)
                Log.d(TAG, "사용자 데이터 변경 감지: $userData")
                trySend(userData)
            }
            
        awaitClose { 
            Log.d(TAG, "사용자 데이터 구독 종료")
            listener.remove() 
        }
    }

    override suspend fun updateUserData(userData: UserData): Boolean {
        return try {
            Log.d(TAG, "사용자 데이터 업데이트: ${userData.userId}")
            
            // 사용자가 처음 생성된 경우 createdAt 필드 유지
            val existingUserData = getUserDataSync(userData.userId)
            val finalData = if (existingUserData == null) {
                // 새 사용자
                userData
            } else {
                // 기존 사용자 - createdAt 유지
                userData.copy(createdAt = existingUserData.createdAt)
            }
            
            firestore.collection(USERS_COLLECTION)
                .document(userData.userId)
                .set(finalData)
                .await()
                
            true
        } catch (e: Exception) {
            Log.e(TAG, "사용자 데이터 업데이트 오류: ${e.message}", e)
            false
        }
    }

    override suspend fun saveUserActivity(userId: String, activity: UserActivity): Boolean {
        return try {
            Log.d(TAG, "사용자 활동 저장: $userId, 유형: ${activity.type}")
            
            val activityWithUserId = if (activity.userId.isEmpty()) {
                activity.copy(userId = userId)
            } else {
                activity
            }
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(ACTIVITIES_COLLECTION)
                .add(activityWithUserId)
                .await()
                
            true
        } catch (e: Exception) {
            Log.e(TAG, "사용자 활동 저장 오류: ${e.message}", e)
            false
        }
    }
    
    /**
     * 동기적으로 사용자 데이터를 조회합니다 (내부 용도)
     */
    private suspend fun getUserDataSync(userId: String): UserData? {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(UserData::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "동기적 사용자 데이터 조회 오류: ${e.message}", e)
            null
        }
    }
} 