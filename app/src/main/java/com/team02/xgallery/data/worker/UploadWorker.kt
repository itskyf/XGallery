package com.team02.xgallery.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.R
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class UploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
            NotificationManager
    private val notificationBuilder =
        NotificationCompat.Builder(applicationContext, AppConstants.UPLOAD_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setGroup(AppConstants.UPLOAD_GROUP)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_delete,
                applicationContext.getString(R.string.cancel),
                WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
            )

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        lateinit var fileName: String
        val resourceUri = Uri.parse(inputData.getString(AppConstants.WORKER_URI))

        // Find file name of uri
        applicationContext.contentResolver.query(
            resourceUri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null
        )?.use { cursor ->
            cursor.moveToFirst()
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        notificationBuilder.setContentTitle(fileName)

        // TODO notification channel?
        val notificationId = inputData.getInt(AppConstants.WORKER_NOTIFICATION_ID, 2929)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AppConstants.UPLOAD_CHANNEL_ID,
                applicationContext.getString(R.string.upload_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        setForeground(ForegroundInfo(notificationId, notificationBuilder.build()))

        val userUID =
            inputData.getString(AppConstants.WORKER_UUID) ?: return@withContext Result.failure()
        Timber.d("workUUID: $id fileName: $fileName")
        Firebase.storage.reference.child("$userUID/media/$id$fileName")
            .putFile(resourceUri)
            .addOnProgressListener {
                notificationManager.notify(
                    notificationId,
                    notificationBuilder.setProgress(
                        it.totalByteCount.toInt(),
                        it.bytesTransferred.toInt(),
                        false
                    ).build()
                )
            }
            .await()
        // TODO handle fail / pause and better notification

        notificationManager.notify(
            notificationId + 1,
            NotificationCompat.Builder(applicationContext, AppConstants.UPLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(fileName)
                .setContentText(applicationContext.getString(R.string.finished))
                .setGroup(AppConstants.UPLOAD_GROUP).build()
        )

        Result.success()
    }
}