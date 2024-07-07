package cz.cvut.fit.podtacky

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.cvut.fit.podtacky.core.di.coreModule
import cz.cvut.fit.podtacky.features.coaster.data.BackupWorker
import cz.cvut.fit.podtacky.features.coaster.di.coasterModule
import cz.cvut.fit.podtacky.features.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, coasterModule, profileModule)
            setupBackupWorker()
        }
    }

    private fun setupBackupWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresDeviceIdle(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(
            periodicWorkRequest
        )
    }
}