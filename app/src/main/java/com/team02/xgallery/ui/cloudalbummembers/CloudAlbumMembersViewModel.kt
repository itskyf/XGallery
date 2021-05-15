package com.team02.xgallery.ui.cloudalbummembers;

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.source.network.CloudAlbumMemberPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CloudAlbumMembersViewModel @Inject constructor(
) : ViewModel() {
    val memberPagingFlow = Pager(
            config = PagingConfig(pageSize = 100),
            pagingSourceFactory = {
                CloudAlbumMemberPagingSource(Firebase.firestore, albumId = "")
            }
    )
}
