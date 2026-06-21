package com.nhut.hoshi.features.reader

import android.os.Build
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReaderImageActionsTest {
    @Test
    fun copyToastIsOnlyShownBeforeSystemClipboardFeedbackExists() {
        assertTrue(shouldShowReaderImageCopyToast(Build.VERSION_CODES.S_V2))
        assertFalse(shouldShowReaderImageCopyToast(Build.VERSION_CODES.TIRAMISU))
        assertFalse(shouldShowReaderImageCopyToast(Build.VERSION_CODES.UPSIDE_DOWN_CAKE))
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
