package com.nhut.hoshi.features.sasayaki

class SasayakiCueUpdateCoordinator(
    private val playbackEvents: SasayakiPlaybackEventCoordinator,
    private val cuePresentation: SasayakiCuePresentationState,
    private val getCurrentChapterIndex: () -> Int,
) {
    fun update(
        hasAudio: Boolean,
        hasMatch: Boolean,
        time: Double,
        delay: Double,
        forceDisplay: Boolean,
        applyCueDisplayAction: (SasayakiCueDisplayAction) -> Unit,
    ) {
        playbackEvents.updateCue(
            hasAudio = hasAudio,
            hasMatch = hasMatch,
            time = time,
            delay = delay,
            currentChapterIndex = getCurrentChapterIndex(),
            autoScroll = cuePresentation.autoScroll,
            hasPlayedOnce = cuePresentation.hasPlayedOnce,
            forceDisplay = forceDisplay,
            applyCueDisplayAction = applyCueDisplayAction,
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
