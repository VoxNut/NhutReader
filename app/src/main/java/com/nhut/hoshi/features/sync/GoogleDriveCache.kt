package com.nhut.hoshi.features.sync

import java.io.File

internal fun googleDriveCoverCacheDirectory(cacheDir: File): File =
    cacheDir.resolve("gdrive-covers")

internal fun clearGoogleDriveCoverCache(cacheDir: File) {
    googleDriveCoverCacheDirectory(cacheDir).deleteRecursively()
}
