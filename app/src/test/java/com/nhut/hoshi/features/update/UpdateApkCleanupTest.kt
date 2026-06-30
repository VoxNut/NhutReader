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
