package com.nhut.hoshi.features.reader

import com.nhut.hoshi.features.sasayaki.SasayakiSettings
import com.nhut.hoshi.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReaderAppearanceSasayakiTest {
    @Test
    fun appearanceShowsStatisticsRowsWhenStatisticsAreEnabled() {
        assertEquals(
            listOf(
                ReaderAppearanceStatisticsRow.Toggle,
                ReaderAppearanceStatisticsRow.ReadingSpeed,
                ReaderAppearanceStatisticsRow.ReadingTime,
            ),
            readerAppearanceStatisticsRows(ReaderSettings(enableStatistics = true)),
        )
    }

    @Test
    fun appearanceHidesStatisticsRowsWhenStatisticsAreDisabled() {
        assertTrue(readerAppearanceStatisticsRows(ReaderSettings(enableStatistics = false)).isEmpty())
    }

    @Test
    fun appearanceHidesProgressPositionWhenProgressIsAlwaysShown() {
        assertTrue(readerAppearanceShowsAlwaysShowProgress(ReaderSettings()))
        assertTrue(!readerAppearanceShowsProgressPosition(ReaderSettings()))
        assertTrue(
            readerAppearanceShowsProgressPosition(
                ReaderSettings(alwaysShowProgress = false),
            ),
        )
        assertTrue(
            !readerAppearanceShowsAlwaysShowProgress(
                ReaderSettings(showCharacters = false, showPercentage = false),
            ),
        )
    }

    @Test
    fun appearanceShowsSasayakiToggleWhenSasayakiIsEnabled() {
        assertEquals(
            listOf(R.string.reader_appearance_show_sasayaki_toggle),
            readerAppearanceSasayakiRows(SasayakiSettings(enabled = true)),
        )
    }

    @Test
    fun appearanceHidesSasayakiToggleWhenSasayakiIsDisabled() {
        assertTrue(readerAppearanceSasayakiRows(SasayakiSettings(enabled = false)).isEmpty())
    }

    @Test
    fun appearanceShowsCustomThemeControlsOnlyForCustomTheme() {
        assertTrue(readerAppearanceShowsCustomInterfaceTheme(ReaderSettings(theme = ReaderTheme.Custom)))
        assertTrue(!readerAppearanceShowsCustomInterfaceTheme(ReaderSettings(theme = ReaderTheme.Sepia)))
        assertEquals(
            listOf(
                ReaderAppearanceCustomColorRow.Background,
                ReaderAppearanceCustomColorRow.Text,
                ReaderAppearanceCustomColorRow.Info,
            ),
            readerAppearanceCustomColorRows(ReaderSettings(theme = ReaderTheme.Custom)),
        )
        assertTrue(readerAppearanceCustomColorRows(ReaderSettings(theme = ReaderTheme.Light)).isEmpty())
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
