package com.statapp.domain.model

/**
 * 별자리 도메인 모델
 *
 * @property id 별자리 ID
 * @property name 별자리 이름
 * @property description 별자리 설명
 * @property stars 별자리를 구성하는 별 목록
 * @property connections 별 사이의 연결 정보
 */
data class Constellation(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val stars: List<Star> = emptyList(),
    val connections: List<StarConnection> = emptyList()
)

/**
 * 별 사이의 연결 정보
 *
 * @property firstStarId 첫 번째 별 ID
 * @property secondStarId 두 번째 별 ID
 */
data class StarConnection(
    val firstStarId: Long,
    val secondStarId: Long
) 