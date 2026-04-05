package com.nhut.hoshi

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.nhut.hoshi.features.reader.ReaderSettings
import com.nhut.hoshi.features.reader.usesDarkInterface
import com.nhut.hoshi.features.reader.usesDarkSystemBarIcons
// import com.nhut.hoshi.features.update.DownloadedUpdatePrompt
// import com.nhut.hoshi.navigation.AppShell
import com.nhut.hoshi.ui.theme.HoshiReaderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject internal lateinit var uiDependencies: HoshiUiDependencies

    private var pendingImportUri by mutableStateOf<Uri?>(null)
    private var readerKeyEventHandler: ((KeyEvent) -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingImportUri = intent.importUri()
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            val readerSettingsRepository = uiDependencies.readerSettingsRepository
            val scope = rememberCoroutineScope()
            var readerSettings by remember { mutableStateOf<ReaderSettings?>(null) }
            LaunchedEffect(readerSettingsRepository) {
                readerSettingsRepository.settings.collect { settings ->
                    readerSettings = settings
                }
            }
            val systemDark = isSystemInDarkTheme()
            val loadedReaderSettings = readerSettings
            val darkTheme = loadedReaderSettings?.usesDarkInterface(systemDark) ?: systemDark
            val useDarkSystemBarIcons = loadedReaderSettings?.usesDarkSystemBarIcons(systemDark) ?: !systemDark
            CompositionLocalProvider(LocalHoshiUiDependencies provides uiDependencies) {
                HoshiReaderTheme(
                    darkTheme = darkTheme,
                    eInkMode = loadedReaderSettings?.eInkMode ?: false,
                    useDarkSystemBarIcons = useDarkSystemBarIcons,
                ) {
                    val loadedReaderSettings = readerSettings ?: return@HoshiReaderTheme
                    // AppShell integration goes here
                    androidx.compose.material3.Text("Hoshi Reader - Khởi tạo UI")
                    // DownloadedUpdatePrompt()
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (readerKeyEventHandler?.invoke(event) == true) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.importUri()?.let { pendingImportUri = it }
    }

    private fun Intent?.importUri(): Uri? =
        this?.data?.takeIf { action == Intent.ACTION_VIEW }
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
