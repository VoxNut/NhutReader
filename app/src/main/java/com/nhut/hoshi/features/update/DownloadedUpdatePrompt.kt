package com.nhut.hoshi.features.update

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.nhut.hoshi.BuildConfig
import com.nhut.hoshi.LocalHoshiUiDependencies
import com.nhut.hoshi.R

@Composable
internal fun DownloadedUpdatePrompt(
    currentVersionName: String = BuildConfig.VERSION_NAME,
    initialRecord: UpdateDownloadRecord? = UpdateStartupSnapshot.initialRecord,
) {
    // Disabled auto update prompt dialogs
}

@Composable
internal fun AvailableUpdatePromptDialog(
    versionName: String,
    message: String?,
    onLater: () -> Unit,
    onSkip: () -> Unit,
    onDownload: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onLater,
        title = { Text(stringResource(R.string.update_available_title)) },
        text = {
            Text(message ?: stringResource(R.string.update_available_message_format, versionName))
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onLater) {
                    Text(stringResource(R.string.action_later))
                }
                TextButton(onClick = onSkip) {
                    Text(stringResource(R.string.action_skip))
                }
                Button(onClick = onDownload) {
                    Text(stringResource(R.string.action_download))
                }
            }
        },
    )
}

internal fun UpdateDownloadRecord.shouldPromptForAvailable(currentVersionName: String): Boolean {
    if (status != UpdateDownloadRecordStatus.Available) return false
    return shouldSurfaceInAbout(currentVersionName)
}

internal fun UpdateDownloadRecord.shouldPromptForManualAvailable(currentVersionName: String): Boolean {
    if (status != UpdateDownloadRecordStatus.Available && status != UpdateDownloadRecordStatus.Skipped) return false
    return shouldSurfaceInAbout(currentVersionName)
}

internal fun UpdateDownloadRecord.shouldPromptForInstall(currentVersionName: String): Boolean {
    if (status != UpdateDownloadRecordStatus.Downloaded) return false
    return shouldSurfaceInAbout(currentVersionName)
}

internal fun UpdateDownloadRecord.shouldSurfaceInAbout(currentVersionName: String): Boolean {
    val downloadedVersion = AppVersion.parse(versionName) ?: return false
    val currentVersion = AppVersion.parse(currentVersionName) ?: return false
    return downloadedVersion > currentVersion
}

internal fun UpdateDownloadRecord.promptKey(): String =
    listOf(versionName, assetName, sha256.orEmpty()).joinToString(separator = "|")

internal fun AvailableUpdate.promptKey(): String =
    listOf(versionName, assetName, sha256.orEmpty()).joinToString(separator = "|")

internal object UpdateStartupSnapshot {
    @Volatile
    var initialRecord: UpdateDownloadRecord? = null
}
