package com.nhut.hoshi.features.reader

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import com.nhut.hoshi.epub.SasayakiMatchData
import com.nhut.hoshi.features.sasayaki.SasayakiCueRange

internal object ReaderSasayakiCues {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun chapterCuesJson(matchData: SasayakiMatchData?, chapterIndex: Int): String? {
        val cues = matchData
            ?.matches
            ?.asSequence()
            ?.filter { it.chapterIndex == chapterIndex }
            ?.sortedBy { it.start }
            ?.map { match ->
                SasayakiCueRange(
                    id = match.id,
                    start = match.start,
                    length = match.length,
                )
            }
            ?.toList()
            ?: return null
        if (cues.isEmpty()) return null
        return json.encodeToString(ListSerializer(SasayakiCueRange.serializer()), cues)
    }
}
