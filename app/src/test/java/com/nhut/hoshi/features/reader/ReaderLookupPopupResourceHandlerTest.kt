package com.nhut.hoshi.features.reader

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReaderLookupPopupResourceHandlerTest {
    @Test
    fun appAssetsRoutesRecognizePopupFontAndImageRequestsForNotFoundFallbacks() {
        assertEquals(
            ReaderLookupPopupAppAssetRoute.Popup,
            readerLookupPopupAppAssetRoute(
                scheme = "https",
                host = "appassets.androidplatform.net",
                path = "/popup/icons/missing.svg",
            ),
        )
        assertEquals(
            ReaderLookupPopupAppAssetRoute.Font,
            readerLookupPopupAppAssetRoute(
                scheme = "https",
                host = "appassets.androidplatform.net",
                path = "/fonts/Missing.ttf",
            ),
        )
        assertEquals(
            ReaderLookupPopupAppAssetRoute.Image,
            readerLookupPopupAppAssetRoute(
                scheme = "https",
                host = "appassets.androidplatform.net",
                path = "/image",
            ),
        )
        assertNull(
            readerLookupPopupAppAssetRoute(
                scheme = "https",
                host = "example.com",
                path = "/fonts/Missing.ttf",
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
