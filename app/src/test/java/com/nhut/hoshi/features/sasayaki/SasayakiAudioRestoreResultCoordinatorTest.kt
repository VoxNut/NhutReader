package com.nhut.hoshi.features.sasayaki

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import com.nhut.hoshi.ui.UiText

class SasayakiAudioRestoreResultCoordinatorTest {
    @Test
    fun failureMarksAudioUnavailableWithErrorMessage() {
        val availability = SasayakiAudioAvailabilityState()
        availability.markRestoreSucceeded()
        val coordinator = SasayakiAudioRestoreResultCoordinator(
            mediaSessionHandle = SasayakiMediaSessionHandleCoordinator(),
            playbackState = SasayakiPlaybackStateCoordinator(initialPosition = 0.0),
            audioAvailability = availability,
        )

        coordinator.handleFailure(IllegalStateException("decode failed"))

        assertFalse(availability.hasAudio)
        assertEquals(UiText.Literal("decode failed"), availability.errorMessage)
    }

    @Test
    fun successReplacesMediaSessionUpdatesDurationAndThenRefreshesCueAndSession() {
        val events = mutableListOf<String>()
        val mediaSessionHandle = SasayakiMediaSessionHandleCoordinator()
        val playbackState = SasayakiPlaybackStateCoordinator(initialPosition = 0.0)
        val availability = SasayakiAudioAvailabilityState()
        val restoredHandle = FakeMediaSessionHandle(events)
        val coordinator = SasayakiAudioRestoreResultCoordinator(
            mediaSessionHandle = mediaSessionHandle,
            playbackState = playbackState,
            audioAvailability = availability,
        )

        coordinator.handleSuccess(
            result = SasayakiAudioRestoreResult(
                mediaSession = restoredHandle,
                durationMs = 12_500,
            ),
            currentTime = 3.25,
            updateCue = { events += "cue:$it" },
            updateMediaSession = {
                events += "session:${playbackState.duration}:${availability.hasAudio}"
                mediaSessionHandle.activate()
            },
        )

        assertEquals(12.5, playbackState.duration, 0.0)
        assertTrue(availability.hasAudio)
        assertNull(availability.errorMessage)
        assertEquals(listOf("cue:3.25", "session:12.5:true", "activate"), events)
    }

    private class FakeMediaSessionHandle(
        private val events: MutableList<String>,
    ) : SasayakiMediaSessionHandle {
        override fun activate() {
            events += "activate"
        }

        override fun update(
            isPlaying: Boolean,
            currentTimeMs: Long,
            durationMs: Long,
            rate: Float,
        ) = Unit

        override fun release() = Unit
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
