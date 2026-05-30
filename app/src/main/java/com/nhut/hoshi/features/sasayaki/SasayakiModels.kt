package com.nhut.hoshi.features.sasayaki

import kotlinx.serialization.Serializable
import com.nhut.hoshi.epub.SasayakiMatchData

data class SasayakiCue(
    val id: String,
    val startTime: Double,
    val endTime: Double,
    val text: String,
)

@Serializable
data class SasayakiCueRange(
    val id: String,
    val start: Int,
    val length: Int,
)

internal fun SasayakiMatchData.matchRateText(): String {
    val matched = matches.size
    val total = matched + unmatched
    val percentage = if (total > 0) matched.toDouble() / total.toDouble() * 100.0 else 0.0
    return "$matched/$total (${String.format(java.util.Locale.US, "%.1f%%", percentage)})"
}

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
