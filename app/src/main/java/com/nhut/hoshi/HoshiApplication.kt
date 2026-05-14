package com.nhut.hoshi

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import com.nhut.hoshi.di.IoDispatcher
import com.nhut.hoshi.features.diagnostics.installCrashDiagnostics
// import com.nhut.hoshi.features.dictionary.DictionaryAutoUpdateScheduler
// import com.nhut.hoshi.features.update.UpdateApkCleanup
// import com.nhut.hoshi.features.update.UpdateScheduler
// import com.nhut.hoshi.features.update.UpdateStartupSnapshot
// import com.nhut.hoshi.features.update.UpdateDownloadStore
import com.google.firebase.FirebaseApp

@HiltAndroidApp
class HoshiApplication : Application(), Configuration.Provider {
    // @Inject internal lateinit var updateApkCleanup: UpdateApkCleanup
    // @Inject internal lateinit var dictionaryAutoUpdateScheduler: Lazy<DictionaryAutoUpdateScheduler>
    // @Inject internal lateinit var updateDownloadStore: UpdateDownloadStore
    // @Inject internal lateinit var updateScheduler: Lazy<UpdateScheduler>
    @Inject internal lateinit var workerFactory: HiltWorkerFactory
    @Inject @IoDispatcher internal lateinit var ioDispatcher: CoroutineDispatcher

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        com.nhut.hoshi.features.firebase.HoshiFirebaseManager.initialize(this)
        installCrashDiagnostics(this)
        // prepareUpdateStartupState()
        // updateScheduler.get().sync()
        // dictionaryAutoUpdateScheduler.get().registerProcessForegroundChecks()
    }

    private fun // prepareUpdateStartupState() {
        UpdateStartupSnapshot.initialRecord = runBlocking(ioDispatcher) {
            updateApkCleanup.deleteCurrentVersionApks()
            updateDownloadStore.load()
        }
    } */
}
