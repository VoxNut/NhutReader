package com.nhut.hoshi.features.reader

internal data class ReaderPositionState(
    val loadPosition: ReaderChapterPosition,
    val displayedPosition: ReaderChapterPosition = loadPosition,
    val loadFragment: String? = null,
) {
    fun recordPageProgress(progress: Double): ReaderPositionState =
        copy(displayedPosition = loadPosition.withProgress(progress))

    fun prepareReloadAtDisplayedPosition(): ReaderPositionState =
        copy(loadPosition = displayedPosition, loadFragment = null)

    fun jumpTo(position: ReaderChapterPosition, fragment: String? = null): ReaderPositionState =
        copy(loadPosition = position, displayedPosition = position, loadFragment = fragment)
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
