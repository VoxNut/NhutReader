package com.nhut.hoshi.epub

import kotlinx.serialization.Serializable

@Serializable
data class SasayakiMatch(
    val id: String,
    val startTime: Double,
    val endTime: Double,
    val text: String,
    val chapterIndex: Int,
    val start: Int,
    val length: Int,
)

@Serializable
data class SasayakiMatchData(
    val matches: List<SasayakiMatch>,
    val unmatched: Int,
)

@Serializable
data class SasayakiPlaybackData(
    val lastPosition: Double,
    val delay: Double = 0.0,
    val rate: Float = 1f,
    val audioUri: String? = null,
    val audioFileName: String? = null,
)

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
