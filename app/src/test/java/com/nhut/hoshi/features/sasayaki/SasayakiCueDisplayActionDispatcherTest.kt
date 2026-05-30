package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiMatch

import org.junit.Assert.assertEquals
import org.junit.Test

class SasayakiCueDisplayActionDispatcherTest {
    private val cue = SasayakiMatch("a", 10.0, 12.0, "a", 1, 2, 3)
    private val events = mutableListOf<String>()
    private val dispatcher = SasayakiCueDisplayActionDispatcher(
        onCue = { cue, reveal -> events += "cue:${cue.id}:$reveal" },
        onClearCue = { events += "clear" },
        onLoadChapter = { chapterIndex -> events += "load:$chapterIndex" },
    )

    @Test
    fun noneDoesNotDispatchReaderCallbacks() {
        dispatcher.apply(SasayakiCueDisplayAction.None)

        assertEquals(emptyList<String>(), events)
    }

    @Test
    fun clearDispatchesOnlyCueClear() {
        dispatcher.apply(SasayakiCueDisplayAction.Clear)

        assertEquals(listOf("clear"), events)
    }

    @Test
    fun displayDispatchesCueAndRevealFlag() {
        dispatcher.apply(SasayakiCueDisplayAction.Display(cue, reveal = true))

        assertEquals(listOf("cue:a:true"), events)
    }

    @Test
    fun clearAndLoadChapterClearsCueBeforeLoadingChapter() {
        dispatcher.apply(SasayakiCueDisplayAction.ClearAndLoadChapter(chapterIndex = 7))

        assertEquals(listOf("clear", "load:7"), events)
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
