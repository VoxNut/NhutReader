package com.nhut.hoshi.features.settings

import com.nhut.hoshi.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdvancedSettingsRowsTest {
    @Test
    fun advancedSettingsRowsMatchIosSectionStructureForSyncAndBackup() {
        val sections = advancedSettingsSections()

        assertEquals(
            listOf(
                listOf(R.string.advanced_audio, R.string.advanced_statistics, R.string.advanced_sasayaki_audiobooks),
                listOf(R.string.sync_ttu_sync, R.string.anki_connect_use),
                listOf(R.string.settings_backup),
                listOf(R.string.advanced_gemini_translation, R.string.advanced_firebase),
            ),
            sections.map { section -> section.rows.map { it.titleRes } },
        )

        val syncRow = sections.flatMap { it.rows }.single { it.destination == AdvancedDestination.Syncing }
        val ankiConnectRow = sections.flatMap { it.rows }.single { it.destination == AdvancedDestination.AnkiConnect }
        val backupRow = sections.flatMap { it.rows }.single { it.destination == AdvancedDestination.Backup }

        assertEquals(AdvancedSettingsIcon.Cloud, syncRow.icon)
        assertEquals(AdvancedSettingsIcon.AnkiConnect, ankiConnectRow.icon)
        assertEquals(AdvancedSettingsIcon.ExternalDrive, backupRow.icon)
        assertFalse(backupRow.icon == AdvancedSettingsIcon.Cloud)
        assertTrue(sections.indexOfFirst { it.rows.any { row -> row.destination == AdvancedDestination.Syncing } } !=
            sections.indexOfFirst { it.rows.any { row -> row.destination == AdvancedDestination.Backup } })
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
