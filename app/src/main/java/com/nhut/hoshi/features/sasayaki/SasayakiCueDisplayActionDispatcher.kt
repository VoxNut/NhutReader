package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiMatch

class SasayakiCueDisplayActionDispatcher(
    private val onCue: (SasayakiMatch, Boolean) -> Unit,
    private val onClearCue: () -> Unit,
    private val onLoadChapter: (Int) -> Unit,
) {
    fun apply(action: SasayakiCueDisplayAction) {
        when (action) {
            SasayakiCueDisplayAction.None -> Unit
            SasayakiCueDisplayAction.Clear -> onClearCue()
            is SasayakiCueDisplayAction.Display -> onCue(action.cue, action.reveal)
            is SasayakiCueDisplayAction.ClearAndLoadChapter -> {
                onClearCue()
                onLoadChapter(action.chapterIndex)
            }
        }
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
