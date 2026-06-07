package com.nhut.hoshi.features.sync

import java.io.File

internal fun googleDriveCoverCacheDirectory(cacheDir: File): File =
    cacheDir.resolve("gdrive-covers")

internal fun clearGoogleDriveCoverCache(cacheDir: File) {
    googleDriveCoverCacheDirectory(cacheDir).deleteRecursively()
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
