package com.nhut.hoshi.features.sasayaki

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SasayakiScreenAwakeTest {
    @Test
    fun keepsScreenAwakeOnlyWhilePlaybackAndAutoScrollAreActiveLikeIos() {
        assertTrue(SasayakiScreenAwake.shouldKeepScreenOn(isPlaying = true, autoScroll = true))
        assertFalse(SasayakiScreenAwake.shouldKeepScreenOn(isPlaying = false, autoScroll = true))
        assertFalse(SasayakiScreenAwake.shouldKeepScreenOn(isPlaying = true, autoScroll = false))
        assertFalse(SasayakiScreenAwake.shouldKeepScreenOn(isPlaying = false, autoScroll = false))
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
