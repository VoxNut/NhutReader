package com.nhut.hoshi.webview

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HoshiWebViewTest {
    @Test
    fun webViewDefaultsDisablePlatformDarkeningForAppControlledThemes() {
        val settings = FakeHoshiWebViewSettings()

        settings.applyHoshiWebViewSecurityDefaults()

        assertTrue(settings.javaScriptEnabled)
        assertFalse(settings.domStorageEnabled)
        assertFalse(settings.allowFileAccess)
        assertFalse(settings.allowContentAccess)
        assertFalse(settings.forceDarkAllowed)
        assertFalse(settings.algorithmicDarkeningAllowed)
    }

    private class FakeHoshiWebViewSettings : HoshiWebViewSettings {
        override var javaScriptEnabled: Boolean = false
        override var domStorageEnabled: Boolean = true
        override var allowFileAccess: Boolean = true
        override var allowContentAccess: Boolean = true
        override var forceDarkAllowed: Boolean = true
        override var algorithmicDarkeningAllowed: Boolean = true
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
