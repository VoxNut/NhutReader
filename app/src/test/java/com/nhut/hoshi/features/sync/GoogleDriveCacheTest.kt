package com.nhut.hoshi.features.sync

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files

class GoogleDriveCacheTest {
    @Test
    fun clearGoogleDriveCoverCacheRemovesRemoteCoverThumbnails() {
        val cacheDir = Files.createTempDirectory("hoshi-drive-cache").toFile()
        val coverCache = googleDriveCoverCacheDirectory(cacheDir)
        coverCache.mkdirs()
        coverCache.resolve("cover-id.jpg").writeText("partial")

        clearGoogleDriveCoverCache(cacheDir)

        assertFalse(coverCache.exists())
    }

    @Test
    fun googleDriveCoverCacheDirectoryMatchesBookshelfRemoteCoverCacheLocation() {
        val cacheDir = Files.createTempDirectory("hoshi-drive-cache-path").toFile()

        val coverCache = googleDriveCoverCacheDirectory(cacheDir)

        assertTrue(coverCache.absolutePath.endsWith("gdrive-covers"))
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
