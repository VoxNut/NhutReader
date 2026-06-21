package com.nhut.hoshi.features.backup

import com.nhut.hoshi.R
import com.nhut.hoshi.importing.UnsupportedImportFileTypeException
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupFailureMessageTest {
    @Test
    fun backupFailuresUseStableFallbackInsteadOfRawExceptionText() {
        val message = RuntimeException("Unsafe TTU bookdata entry: ../bad.zip").stableBackupFailureMessage(
            fallback = "Unable to import TTU bookdata backup.",
            unsupportedImportMessage = "Select a .zip TTU bookdata backup file.",
        )

        assertEquals("Unable to import TTU bookdata backup.", message)
    }

    @Test
    fun backupFailuresCanStillUseResourceBackedUnsupportedImportMessages() {
        val message = UnsupportedImportFileTypeException(
            message = "Select a .zip TTU bookdata backup file.",
            messageRes = R.string.import_select_ttu_bookdata_backup,
        ).stableBackupFailureMessage(
            fallback = "Unable to import TTU bookdata backup.",
            unsupportedImportMessage = "Select a .zip TTU bookdata backup file.",
        )

        assertEquals("Select a .zip TTU bookdata backup file.", message)
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
