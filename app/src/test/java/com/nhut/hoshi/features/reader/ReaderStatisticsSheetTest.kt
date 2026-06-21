package com.nhut.hoshi.features.reader

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReaderStatisticsSheetTest {
    @Test
    fun statisticsSheetDoesNotRenderASeparateHeaderOrCloseButton() {
        val chrome = readerStatisticsSheetChrome()

        assertFalse(chrome.showHeader)
        assertFalse(chrome.showCloseButton)
    }

    @Test
    fun statisticsSheetUsesReaderPanelChrome() {
        val chrome = readerStatisticsSheetChrome()

        assertTrue(chrome.opensAsReaderPanel)
    }

    @Test
    fun readerPanelHeightLeavesTopThirtyPercentAsDismissArea() {
        assertEquals(
            700f,
            readerPanelHeight(containerHeight = 1000f),
        )
    }

    @Test
    fun readerPanelHandleDragUsesSingleHeightOrDismisses() {
        val targetHeight = 700f
        val threshold = 96f

        assertEquals(
            targetHeight,
            readerPanelSettleTarget(
                currentHeight = 720f,
                totalDrag = -120f,
                targetHeight = targetHeight,
                threshold = threshold,
            ),
        )
        assertEquals(
            targetHeight,
            readerPanelSettleTarget(
                currentHeight = 640f,
                totalDrag = 64f,
                targetHeight = targetHeight,
                threshold = threshold,
            ),
        )
        assertNull(
            readerPanelSettleTarget(
                currentHeight = 560f,
                totalDrag = 160f,
                targetHeight = targetHeight,
                threshold = threshold,
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
