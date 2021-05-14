package com.team02.xgallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.team02.xgallery.data.repository.UserRepository
import com.team02.xgallery.data.worker.UploadWorker
import com.team02.xgallery.utils.AppConstants
import com.team02.xgallery.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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


    fun uploadFiles(uris: List<Uri>) {
        if (uris.isNotEmpty()) {
            workManager.beginWith(uris.map { uri ->
                OneTimeWorkRequestBuilder<UploadWorker>()
                    .setInputData(
                        workDataOf(
                            AppConstants.WORKER_URI to uri.toString(),
                            AppConstants.WORKER_UUID to userRepository.userUID,
                            AppConstants.WORKER_NOTIFICATION_ID to Utils.notificationCounter
                        )
                    )
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .addTag(AppConstants.WORKER_UPLOAD_TAG)
                    .build()
            }).enqueue()
        }
    }
}