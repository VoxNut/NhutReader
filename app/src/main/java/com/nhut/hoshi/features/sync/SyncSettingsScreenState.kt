package com.nhut.hoshi.features.sync

import com.nhut.hoshi.features.settings.settingsContentReady

internal data class SyncSettingsScreenState(
    val settings: SyncSettings?,
    val authStatus: DriveAuthStatus?,
) {
    val isContentReady: Boolean
        get() = settingsContentReady(settings, authStatus)

    val showClearCacheAction: Boolean
        get() = isContentReady && settings?.enabled == true && authStatus == DriveAuthStatus.Connected
}
