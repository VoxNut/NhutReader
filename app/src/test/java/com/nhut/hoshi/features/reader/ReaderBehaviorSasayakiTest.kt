package com.nhut.hoshi.features.reader

import com.nhut.hoshi.R
import org.junit.Assert.assertEquals
import org.junit.Test

class ReaderBehaviorSasayakiTest {
    @Test
    fun behaviorShowsKeepScreenOnBeforeSasayakiVolumeSeek() {
        assertEquals(
            listOf(
                R.string.reader_behavior_volume_keys_turn_pages,
                R.string.reader_behavior_volume_keys_seek_sasayaki,
                R.string.reader_behavior_reverse_volume_key_direction,
                R.string.reader_behavior_keep_screen_on,
                R.string.reader_behavior_auto_check_updates,
            ),
            readerBehaviorRows(),
        )
    }

    @Test
    fun behaviorAlwaysShowsSasayakiVolumeSeek() {
        assertEquals(
            listOf(R.string.reader_behavior_volume_keys_seek_sasayaki),
            readerBehaviorSasayakiRows(),
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
