package com.nhut.hoshi.features.update

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateApkCleanupTest {
    @Test
    fun deletesOnlyApksForTheCurrentlyInstalledHoshiVersion() {
        assertTrue(
            shouldDeleteUpdateApk(
                archiveInfo = UpdateApkArchiveInfo(
                    packageName = "com.nhut.hoshi",
                    versionName = "0.6.1",
                ),
                currentPackageName = "com.nhut.hoshi",
                currentVersionName = "0.6.1",
            ),
        )
        assertFalse(
            shouldDeleteUpdateApk(
                archiveInfo = UpdateApkArchiveInfo(
                    packageName = "com.nhut.hoshi",
                    versionName = "0.6.2",
                ),
                currentPackageName = "com.nhut.hoshi",
                currentVersionName = "0.6.1",
            ),
        )
        assertFalse(
            shouldDeleteUpdateApk(
                archiveInfo = UpdateApkArchiveInfo(
                    packageName = "com.example.other",
                    versionName = "0.6.1",
                ),
                currentPackageName = "com.nhut.hoshi",
                currentVersionName = "0.6.1",
            ),
        )
        assertFalse(
            shouldDeleteUpdateApk(
                archiveInfo = null,
                currentPackageName = "com.nhut.hoshi",
                currentVersionName = "0.6.1",
            ),
        )
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
