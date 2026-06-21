package com.nhut.hoshi.features.anki

import android.net.Uri
import androidx.core.content.FileProvider

class HoshiFileProvider : FileProvider() {
    override fun getType(uri: Uri): String? =
        when (uri.lastPathSegment?.substringAfterLast('.', missingDelimiterValue = "")?.lowercase()) {
            "aac" -> "audio/aac"
            "m4a" -> "audio/mp4"
            else -> super.getType(uri)
        }
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
