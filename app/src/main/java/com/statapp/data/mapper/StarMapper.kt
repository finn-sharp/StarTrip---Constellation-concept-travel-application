package com.statapp.data.mapper

import com.statapp.data.local.BusinessHoursEntity
import com.statapp.data.local.StarEntity
import com.statapp.domain.model.BusinessHours
import com.statapp.domain.model.Star
import java.time.LocalTime

/**
 * StarEntity와 Star 도메인 모델 간 변환을 담당하는 매퍼 함수들입니다.
 */

/**
 * Star 도메인 모델을 StarEntity로 변환합니다.
 */
fun Star.toEntity(): StarEntity {
    return StarEntity(
        id = id,
        placeId = placeId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        businessHours = businessHours.mapValues { (_, hours) ->
            BusinessHoursEntity(
                openTimeMinutes = hours.openTime?.toMinuteOfDay(),
                closeTimeMinutes = hours.closeTime?.toMinuteOfDay(),
                isOpen24Hours = hours.isOpen24Hours,
                isClosed = hours.isClosed
            )
        },
        isActive = isActive,
        isFavorite = isFavorite
    )
}

/**
 * StarEntity를 Star 도메인 모델로 변환합니다.
 */
fun StarEntity.toDomain(): Star {
    return Star(
        id = id,
        placeId = placeId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        businessHours = businessHours.mapValues { (_, hours) ->
            BusinessHours(
                openTime = hours.openTimeMinutes?.let { LocalTime.ofSecondOfDay((it * 60).toLong()) },
                closeTime = hours.closeTimeMinutes?.let { LocalTime.ofSecondOfDay((it * 60).toLong()) },
                isOpen24Hours = hours.isOpen24Hours,
                isClosed = hours.isClosed
            )
        },
        isActive = isActive,
        isFavorite = isFavorite
    )
}

/**
 * LocalTime을 하루 중 분 단위로 변환합니다.
 */
private fun LocalTime.toMinuteOfDay(): Int {
    return this.hour * 60 + this.minute
}