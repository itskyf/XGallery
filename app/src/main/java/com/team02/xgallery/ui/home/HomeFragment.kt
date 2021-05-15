package com.team02.xgallery.ui.home

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.MainActivity
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentHomeBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.adapter.StoryAdapter
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController
    private var selectionMode: ActionMode? = null
    private var onSelectionModeJob: Job? = null
    private var selectedCountJob: Job? = null
    private val listImg = arrayListOf<String>()
    private val listTitle = arrayListOf<String>()
    private val userUid = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userUid != "null") {
            db.collection("users").document(userUid).get().addOnSuccessListener{document->
                val memories1 = document.data?.get("memories1") as ArrayList<String>
                val memories2 = document.data?.get("memories2") as ArrayList<String>
                val memories3 = document.data?.get("memories3") as ArrayList<String>
                val memories4 = document.data?.get("memories4") as ArrayList<String>
                val memories5 = document.data?.get("memories5") as ArrayList<String>
                if(memories1.size!=0){
                    if(!("1 years ago" in listTitle && memories1[0] in listImg))
                        listTitle.add("1 years ago")
                    listImg.add(memories1[0])
                }
                if(memories2.size!=0){
                    if(!("2 years ago" in listTitle && memories2[0] in listImg))
                        listTitle.add("2 years ago")
                    listImg.add(memories2[0])
                }
                if(memories3.size!=0){
                    if(!("3 years ago" in listTitle && memories3[0] in listImg))
                        listTitle.add("3 years ago")
                    listImg.add(memories3[0])
                }
                if(memories4.size!=0){
                    if(!("4 years ago" in listTitle && memories4[0] in listImg))
                        listTitle.add("4 years ago")
                    listImg.add(memories4[0])
                }
                if(memories5.size!=0){
                    if(!("5 years ago" in listTitle && memories5[0] in listImg))
                        listTitle.add("5 years ago")
                    listImg.add(memories5[0])
                }
                val storyAdapter = StoryAdapter({
                    navController.navigate(HomeFragmentDirections.openStory(it))
                }, listImg, listTitle)
                binding.storyList.adapter = storyAdapter
                binding.storyList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                if (memories1.size == 0 && memories2.size == 0 && memories3.size == 0 && memories4.size == 0 && memories5.size == 0) {
                    binding.storyList.visibility = View.GONE
                } else {
                    binding.storyList.visibility = View.VISIBLE
                }
            }
        }

        val cloudMediaPagingAdapter = CloudMediaAdapter({
            navController.navigate(HomeFragmentDirections.openCloudPhotoViewFromHome(it.id!!))
        }, viewModel.selectionManager)
        binding.mediaGrid.adapter = cloudMediaPagingAdapter
        binding.mediaGrid.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding.mediaGrid.addItemDecoration(
            ItemDecoration(resources.getDimension(R.dimen.small_padding), 3)
        )

        lifecycleScope.launch {
            viewModel.mediaPagingFlow.collectLatest { pagingData ->
                cloudMediaPagingAdapter.submitData(pagingData)
            }
        }

        onSelectionModeJob = lifecycleScope.launch {
            viewModel.selectionManager.onSelectionMode.collectLatest { onSelectionMode ->
                if (onSelectionMode) {
                    selectionMode =
                        (requireActivity() as MainActivity).startSupportActionMode(callback) as ActionMode
                } else {
                    selectionMode?.finish()
                }
                cloudMediaPagingAdapter.notifyDataSetChanged()
            }
        }

        selectedCountJob = lifecycleScope.launch {
            viewModel.selectionManager.selectedCount.collectLatest { selectedCount ->
                selectionMode?.title = "$selectedCount"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        onSelectionModeJob?.cancel()
        selectedCountJob?.cancel()
        super.onStop()
    }

    private val callback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.device_album_contextual_top_app_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem?
        ): Boolean {
            return when (item?.itemId) {
                R.id.share -> {
                    val arrayImage = viewModel.selectionManager.getItemKeyList()
                    val imageUris: ArrayList<Uri> = ArrayList()
                    for (i in arrayImage) {
                        Uri.parse(i.toString())
                    }
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND_MULTIPLE
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
                        type = "image/*"
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share images to.."))
                    true
                }
                R.id.delete -> {
                    true
                }
                R.id.more -> {
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            viewModel.selectionManager.clear()
        }
    }
}