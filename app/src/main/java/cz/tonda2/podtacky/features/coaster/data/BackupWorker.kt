package cz.tonda2.podtacky.features.coaster.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BackupWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val backupManager: cz.tonda2.podtacky.core.data.BackupManager by inject()

    override suspend fun doWork(): Result {
        backup()
        return Result.success()
    }

    private suspend fun backup() {
        Log.d("BACKUP WORKER", "Starting periodic backup")
        backupManager.createBackup()
    }
}