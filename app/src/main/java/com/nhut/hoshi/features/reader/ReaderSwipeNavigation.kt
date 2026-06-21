package com.nhut.hoshi.features.reader

internal enum class ReaderSwipeDirection {
    Left,
    Right,
}

internal fun readerNavigationDirectionForSwipe(
    isVerticalWriting: Boolean,
    swipeDirection: ReaderSwipeDirection,
): ReaderNavigationDirection =
    when (swipeDirection) {
        ReaderSwipeDirection.Left -> {
            if (isVerticalWriting) {
                ReaderNavigationDirection.Backward
            } else {
                ReaderNavigationDirection.Forward
            }
        }
        ReaderSwipeDirection.Right -> {
            if (isVerticalWriting) {
                ReaderNavigationDirection.Forward
            } else {
                ReaderNavigationDirection.Backward
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
