package com.team02.xgallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.team02.xgallery.data.repository.UserRepository
import com.team02.xgallery.data.worker.UploadWorker
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val userRepository: UserRepository
) : ViewModel() {
    val authStateFlow = userRepository.authStateFlow
    val isAvailableToLogIn
        get() = userRepository.isAvailableToLogIn
    val worksFlow = workManager.getWorkInfosByTagLiveData(AppConstants.WORKER_UPLOAD_TAG).asFlow()

    init {
        viewModelScope.launch {
            worksFlow.collectLatest {
                workManager.pruneWork()
            }
        }
    }

    // TODO remove LiveData when Android support
    private val notificationId = AtomicInteger(1)
    private val uploadConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    fun uploadFiles(uris: List<Uri>) {
        if (uris.isNotEmpty()) {
            workManager.beginWith(uris.map { uri ->
                OneTimeWorkRequestBuilder<UploadWorker>()
                    .setInputData(
                        workDataOf(
                            AppConstants.WORKER_URI to uri.toString(),
                            AppConstants.WORKER_UUID to userRepository.userUID,
                            AppConstants.WORKER_NOTIFICATION_ID to notificationId.getAndAdd(2)
                        )
                    )
                    .setConstraints(uploadConstraints)
                    .addTag(AppConstants.WORKER_UPLOAD_TAG)
                    .build()
            }).enqueue()
        }
    }
}