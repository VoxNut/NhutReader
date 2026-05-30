package com.nhut.hoshi.features.sasayaki

import org.junit.Assert.assertEquals
import org.junit.Test

class SasayakiSheetTest {
    @Test
    fun playbackSpeedSliderAllowsTwoTimesSpeedWithExistingStepSize() {
        assertEquals(0.5f, SasayakiSpeedSliderRange.start, 0.0f)
        assertEquals(2.0f, SasayakiSpeedSliderRange.endInclusive, 0.0f)

        val intervalCount = SasayakiSpeedSliderSteps + 1
        val stepSize = (SasayakiSpeedSliderRange.endInclusive - SasayakiSpeedSliderRange.start) / intervalCount
        assertEquals(0.05f, stepSize, 0.0001f)
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
