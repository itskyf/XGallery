package com.team02.xgallery

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.repository.UserRepository
import com.team02.xgallery.data.worker.UploadWorker
import com.team02.xgallery.utils.AppConstants
import com.team02.xgallery.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

    fun memories(){
        val m = HashMap<String, Any>()
        val n = HashMap<String, String>()
        n["0"]=""
        m["memories1"] = n
        m["memories2"] = n
        m["memories3"] = n
        m["memories4"] = n
        m["memories5"] = n
        val db = Firebase.firestore
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
        val dateOnly: String = dateFormat.format(currentDate)
        val day: String = dateOnly.substring(0,2)
        val month: String = dateOnly.substring(3,6)
        val year: String = dateOnly.substring(7,11)
        val today = "$day $month $year"
        val yearInt: Int = Integer.parseInt(year)
        val memoryDay = "$day $month"
        //val memoryDay = "24 Oct"
        val memories1 = arrayListOf<String>()
        val memories2 = arrayListOf<String>()
        val memories3 = arrayListOf<String>()
        val memories4 = arrayListOf<String>()
        val memories5 = arrayListOf<String>()
        db.collection("users").document(userRepository.userUID.toString()).get().addOnSuccessListener {document->
            val date: Date? = document.getTimestamp("lastSeen")?.toDate()
            val lastSeen:String = date.toString()
            val lastSeenDay = lastSeen.substring(8, 10) + " " + lastSeen.substring(4, 7) + " " + lastSeen.substring(30, 34)
            if(lastSeenDay != today){
                db.collection("users").document(userRepository.userUID.toString()).set(m, SetOptions.merge())
                db.collection("media").whereEqualTo("owner", userRepository.userUID.toString()).get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val date: Date? = document.getTimestamp("dateTaken")?.toDate()
                        val stringDate = date.toString()
                        val dateOfImg = stringDate.substring(8, 10) + " " + stringDate.substring(4, 7)
                        val yearOfImg = Integer.parseInt(stringDate.substring(30, 34))
                        if(memoryDay == dateOfImg) {
                            when(yearInt-yearOfImg){
                                1 -> memories1.add(document.data["id"] as String)
                                2 -> memories2.add(document.data["id"] as String)
                                3 -> memories3.add(document.data["id"] as String)
                                4 -> memories4.add(document.data["id"] as String)
                                5 -> memories5.add(document.data["id"] as String)
                            }

                        }
                    }
                    db.collection("users").document(userRepository.userUID.toString()).update("memories1", memories1)
                    db.collection("users").document(userRepository.userUID.toString()).update("memories2", memories2)
                    db.collection("users").document(userRepository.userUID.toString()).update("memories3", memories3)
                    db.collection("users").document(userRepository.userUID.toString()).update("memories4", memories4)
                    db.collection("users").document(userRepository.userUID.toString()).update("memories5", memories5)
                    db.collection("users").document(userRepository.userUID.toString()).update("lastSeen", Timestamp.now())
                }.addOnFailureListener {
                    Log.d("Sang", "Error getting documents: ")
                }
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