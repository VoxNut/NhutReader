package com.nhut.hoshi.features.reader

import com.nhut.hoshi.features.sasayaki.SasayakiScreenAwake

object ReaderScreenAwake {
    fun shouldKeepScreenOn(
        keepScreenOnWhileReading: Boolean,
        sasayakiIsPlaying: Boolean,
        sasayakiAutoScroll: Boolean,
    ): Boolean =
        keepScreenOnWhileReading ||
            SasayakiScreenAwake.shouldKeepScreenOn(
                isPlaying = sasayakiIsPlaying,
                autoScroll = sasayakiAutoScroll,
            )
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
