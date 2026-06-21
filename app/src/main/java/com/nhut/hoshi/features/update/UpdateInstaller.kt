package com.nhut.hoshi.features.update

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

internal fun openDownloadedUpdate(context: Context, file: File): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.packageManager.canRequestPackageInstalls()) {
        val settingsIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(Uri.parse("package:${context.packageName}"))
        return runCatching {
            context.startActivity(settingsIntent)
            "Allow Nhut Reader to install unknown apps, then tap Install again."
        }.getOrElse {
            "Allow Nhut Reader to install unknown apps in Android settings, then tap Install again."
        }
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file,
    )
    val intent = Intent(Intent.ACTION_VIEW)
        .setDataAndType(uri, AndroidUpdateDownloadManager.ApkMimeType)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.clipData = ClipData.newUri(context.contentResolver, "Nhut Reader update", uri)
    return try {
        context.startActivity(intent)
        null
    } catch (_: ActivityNotFoundException) {
        "No APK installer is available on this device."
    } catch (_: SecurityException) {
        "Android blocked the package installer. Allow Nhut Reader to install unknown apps, then tap Install again."
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
