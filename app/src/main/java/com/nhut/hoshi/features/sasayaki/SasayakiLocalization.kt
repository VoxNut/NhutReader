package com.nhut.hoshi.features.sasayaki

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nhut.hoshi.R

@Composable
internal fun SasayakiReaderSkipButtonAction.labelText(): String =
    seconds?.let { stringResource(R.string.sasayaki_skip_seconds_format, it) }
        ?: stringResource(R.string.sasayaki_skip_cue)

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
