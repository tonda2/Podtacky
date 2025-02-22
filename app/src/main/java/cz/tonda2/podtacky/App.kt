package cz.tonda2.podtacky

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.tonda2.podtacky.core.di.coreModule
import cz.tonda2.podtacky.features.coaster.data.BackupWorker
import cz.tonda2.podtacky.features.coaster.di.coasterModule
import cz.tonda2.podtacky.features.folder.di.folderModule
import cz.tonda2.podtacky.features.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, coasterModule, profileModule, folderModule)
            setupBackupWorker()
        }
    }

    private fun setupBackupWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            // .setRequiresDeviceIdle(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "backup_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}