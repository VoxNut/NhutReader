package com.nhut.hoshi.features.reader

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.nhut.hoshi.epub.SasayakiMatch
import com.nhut.hoshi.epub.SasayakiMatchData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReaderSasayakiCuesTest {
    @Test
    fun chapterCuesSerializesOnlyCurrentChapterCuesSortedByStart() {
        val matchData = SasayakiMatchData(
            matches = listOf(
                match(id = "later", chapterIndex = 1, start = 20, length = 3),
                match(id = "other", chapterIndex = 0, start = 1, length = 4),
                match(id = "first", chapterIndex = 1, start = 10, length = 2),
            ),
            unmatched = 0,
        )

        val json = ReaderSasayakiCues.chapterCuesJson(matchData, chapterIndex = 1)
        val cues = Json.parseToJsonElement(requireNotNull(json)).jsonArray

        assertEquals(listOf("first", "later"), cues.map { it.jsonObject.getValue("id").jsonPrimitive.content })
        assertEquals(listOf("10", "20"), cues.map { it.jsonObject.getValue("start").jsonPrimitive.content })
        assertEquals(listOf("2", "3"), cues.map { it.jsonObject.getValue("length").jsonPrimitive.content })
    }

    @Test
    fun chapterCuesReturnsNullWhenChapterHasNoMatches() {
        val matchData = SasayakiMatchData(
            matches = listOf(match(id = "other", chapterIndex = 0, start = 1, length = 4)),
            unmatched = 0,
        )

        assertNull(ReaderSasayakiCues.chapterCuesJson(matchData, chapterIndex = 1))
    }

    private fun match(
        id: String,
        chapterIndex: Int,
        start: Int,
        length: Int,
    ): SasayakiMatch =
        SasayakiMatch(
            id = id,
            startTime = start.toDouble(),
            endTime = start.toDouble() + 1,
            text = id,
            chapterIndex = chapterIndex,
            start = start,
            length = length,
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
