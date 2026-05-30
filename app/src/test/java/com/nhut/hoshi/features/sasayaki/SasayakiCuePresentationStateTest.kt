package com.nhut.hoshi.features.sasayaki

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SasayakiCuePresentationStateTest {
    @Test
    fun defaultsToAutoScrollBeforeFirstPlayback() {
        val state = SasayakiCuePresentationState()

        assertTrue(state.autoScroll)
        assertFalse(state.hasPlayedOnce)
    }

    @Test
    fun autoScrollRemainsComposeObservedAndWritable() {
        val state = SasayakiCuePresentationState()

        state.autoScroll = false

        assertFalse(state.autoScroll)
    }

    @Test
    fun markPlayedOnceOnlyEnablesFirstPlayState() {
        val state = SasayakiCuePresentationState()

        state.markPlayedOnce()

        assertTrue(state.hasPlayedOnce)
        assertTrue(state.autoScroll)
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
