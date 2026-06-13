package com.nhut.hoshi.features.sync

import com.nhut.hoshi.R
import com.nhut.hoshi.features.reader.ReaderSettings
import com.nhut.hoshi.features.sasayaki.SasayakiSettings
import org.junit.Assert.assertEquals
import org.junit.Test

class SyncSettingsDataRowsTest {
    @Test
    fun dataRowsMatchIosUploadStatsAndAudiobookToggles() {
        val rows = syncSettingsDataRows(
            syncSettings = SyncSettings(enabled = true, uploadBooks = true),
            readerSettings = ReaderSettings(enableStatistics = true, statisticsSyncEnabled = false),
            sasayakiSettings = SasayakiSettings(enabled = true, syncEnabled = true),
        )

        assertEquals(
            listOf(
                R.string.sync_upload_books,
                R.string.sync_stats,
                R.string.sync_audiobook_progress,
            ),
            rows.map { it.titleRes },
        )
        assertEquals(listOf(true, false, true), rows.map { it.checked })
    }

    @Test
    fun dataRowsHideStatsAndAudiobookWhenTheirFeaturesAreDisabledLikeIos() {
        val rows = syncSettingsDataRows(
            syncSettings = SyncSettings(enabled = true, uploadBooks = false),
            readerSettings = ReaderSettings(enableStatistics = false, statisticsSyncEnabled = true),
            sasayakiSettings = SasayakiSettings(enabled = false, syncEnabled = true),
        )

        assertEquals(listOf(R.string.sync_upload_books), rows.map { it.titleRes })
        assertEquals(listOf(false), rows.map { it.checked })
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
