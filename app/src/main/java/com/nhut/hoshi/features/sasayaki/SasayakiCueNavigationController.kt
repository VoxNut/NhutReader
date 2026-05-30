package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiMatchData
import com.nhut.hoshi.epub.SasayakiMatch

import kotlin.math.max

class SasayakiCueNavigationController(matchData: SasayakiMatchData?) {
    private val timeline = CueTimeline(matchData)

    fun nextCueSeekTime(
        currentTime: Double,
        delay: Double,
    ): Double? {
        val playbackTime = currentTime - delay
        val anchor = timeline.cueAt(playbackTime)?.startTime ?: playbackTime
        val next = timeline.nextCue(after = anchor) ?: return null
        return next + delay
    }

    fun previousCueSeekTime(
        currentTime: Double,
        delay: Double,
    ): Double {
        val playbackTime = max(0.0, currentTime - delay)
        val anchor = timeline.cueAt(playbackTime)?.startTime ?: playbackTime
        val previous = timeline.previousCue(before = anchor) ?: 0.0
        return previous + delay
    }

    fun cueAtPlaybackTime(time: Double, delay: Double): SasayakiMatch? =
        timeline.cueAt(time - delay)

    fun findCue(chapterIndex: Int, offset: Int): SasayakiMatch? =
        timeline.findCue(chapterIndex = chapterIndex, offset = offset)
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
