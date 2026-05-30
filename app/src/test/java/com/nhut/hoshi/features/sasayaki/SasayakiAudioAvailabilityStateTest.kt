package com.nhut.hoshi.features.sasayaki

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import com.nhut.hoshi.R
import com.nhut.hoshi.ui.UiText

class SasayakiAudioAvailabilityStateTest {
    @Test
    fun restoreSuccessMarksAudioAvailableAndClearsError() {
        val state = SasayakiAudioAvailabilityState()
        state.markRestoreFailed(IllegalStateException("missing"))

        state.markRestoreSucceeded()

        assertTrue(state.hasAudio)
        assertNull(state.errorMessage)
    }

    @Test
    fun restoreFailureMarksAudioUnavailableAndKeepsReadableMessage() {
        val state = SasayakiAudioAvailabilityState()
        state.markRestoreSucceeded()

        state.markRestoreFailed(IllegalStateException("bad audio"))

        assertFalse(state.hasAudio)
        assertEquals(UiText.Literal("bad audio"), state.errorMessage)
    }

    @Test
    fun restoreFailureUsesFallbackMessageWhenErrorHasNoMessage() {
        val state = SasayakiAudioAvailabilityState()

        state.markRestoreFailed(object : Throwable() {})

        assertFalse(state.hasAudio)
        assertEquals(UiText.Resource(R.string.sasayaki_import_audiobook_failed), state.errorMessage)
    }

    @Test
    fun clearAudioClearsErrorButTeardownOnlyMarksAudioUnavailable() {
        val state = SasayakiAudioAvailabilityState()
        state.markRestoreFailed(IllegalStateException("still visible"))

        state.markAudioUnavailable()

        assertFalse(state.hasAudio)
        assertEquals(UiText.Literal("still visible"), state.errorMessage)

        state.markAudioCleared()

        assertFalse(state.hasAudio)
        assertNull(state.errorMessage)
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
