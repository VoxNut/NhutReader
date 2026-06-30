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
