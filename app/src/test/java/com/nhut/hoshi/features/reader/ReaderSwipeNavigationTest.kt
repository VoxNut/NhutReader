package com.nhut.hoshi.features.reader

import org.junit.Assert.assertEquals
import org.junit.Test

class ReaderSwipeNavigationTest {
    @Test
    fun horizontalWritingUsesLeftSwipeForNextPageLikeIos() {
        assertEquals(
            ReaderNavigationDirection.Forward,
            readerNavigationDirectionForSwipe(
                isVerticalWriting = false,
                swipeDirection = ReaderSwipeDirection.Left,
            ),
        )
        assertEquals(
            ReaderNavigationDirection.Backward,
            readerNavigationDirectionForSwipe(
                isVerticalWriting = false,
                swipeDirection = ReaderSwipeDirection.Right,
            ),
        )
    }

    @Test
    fun verticalWritingUsesRightSwipeForNextPageLikeIos() {
        assertEquals(
            ReaderNavigationDirection.Backward,
            readerNavigationDirectionForSwipe(
                isVerticalWriting = true,
                swipeDirection = ReaderSwipeDirection.Left,
            ),
        )
        assertEquals(
            ReaderNavigationDirection.Forward,
            readerNavigationDirectionForSwipe(
                isVerticalWriting = true,
                swipeDirection = ReaderSwipeDirection.Right,
            ),
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
