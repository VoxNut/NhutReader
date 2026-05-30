package com.nhut.hoshi.features.sasayaki

import android.content.Context
import androidx.media3.common.Player
import java.io.File

interface SasayakiMediaSessionHandle {
    fun activate()

    fun update(
        isPlaying: Boolean,
        currentTimeMs: Long,
        durationMs: Long,
        rate: Float,
    )

    fun release()
}

class AndroidSasayakiMediaSessionHandle(
    context: Context,
    player: Player,
    title: String,
    artworkFile: File?,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipToPrevious: () -> Unit,
    onSkipToNext: () -> Unit,
    onSeekTo: (Long) -> Unit,
) : SasayakiMediaSessionHandle {
    private val session = SasayakiMediaSession(
        context = context,
        player = player,
        title = title,
        artwork = SasayakiMediaSession.loadCoverArt(artworkFile),
        onPlay = onPlay,
        onPause = onPause,
        onSkipToPrevious = onSkipToPrevious,
        onSkipToNext = onSkipToNext,
        onSeekTo = onSeekTo,
    )

    override fun activate() {
        session.activate()
    }

    override fun update(
        isPlaying: Boolean,
        currentTimeMs: Long,
        durationMs: Long,
        rate: Float,
    ) {
        session.update(
            isPlaying = isPlaying,
            currentTimeMs = currentTimeMs,
            durationMs = durationMs,
            rate = rate,
        )
    }

    override fun release() {
        session.release()
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
