package com.nhut.hoshi.webview

import android.view.View
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HoshiWebViewTest {
    @Test
    fun hoshiWebViewsDisableNativeOverscrollStretch() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext

        instrumentation.runOnMainSync {
            val webView = WebView(context)

            webView.disableNativeOverscrollStretch()

            assertEquals(View.OVER_SCROLL_NEVER, webView.overScrollMode)
        }
    }

    @Test
    fun hoshiWebViewsApplySecureReaderAndPopupDefaults() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext

        instrumentation.runOnMainSync {
            val webView = WebView(context)

            webView.applyHoshiWebViewSecurityDefaults()

            assertTrue(webView.settings.javaScriptEnabled)
            assertFalse(webView.settings.domStorageEnabled)
            assertFalse(webView.settings.allowFileAccess)
            assertFalse(webView.settings.allowContentAccess)
            assertEquals(View.OVER_SCROLL_NEVER, webView.overScrollMode)
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
