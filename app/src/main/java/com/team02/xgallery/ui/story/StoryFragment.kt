package com.team02.xgallery.ui.story

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.FragmentStoryBinding
import com.team02.xgallery.utils.GlideApp
import dagger.hilt.android.AndroidEntryPoint
import pt.tornelas.segmentedprogressbar.SegmentedProgressBarListener

@AndroidEntryPoint
class StoryFragment() : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var listImage = arrayListOf<String>()
    private var pos = 0
    private val userUid = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: StoryFragmentArgs by navArgs()
        val year = args.year
        db.collection("users").document(userUid).get().addOnSuccessListener{document->
            val memories = document.data?.get("memories$year") as ArrayList<String>
            for(memory in memories){
                listImage.add(memory)
            }
            binding.spb.segmentCount = listImage.size
            GlideApp.with(binding.storyImg).load(Firebase.storage.getReference(listImage[pos])).into(binding.storyImg)
            binding.leftBtn.setOnClickListener{
                if(pos == 0)
                {
                    binding.spb.restartSegment()
                }else {
                    binding.spb.previous()
                }
            }
            binding.rightBtn.setOnClickListener{
                binding.spb.next()
            }
            binding.storyLayout.setOnTouchListener(View.OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    binding.spb.pause()
                    return@OnTouchListener true
                } else if (event.action == MotionEvent.ACTION_UP) {
                    binding.spb.start()
                    return@OnTouchListener true
                }
                false
            })
            binding.spb.listener = object : SegmentedProgressBarListener {
                override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
                    GlideApp.with(binding.storyImg).load(Firebase.storage.getReference(listImage[pos])).into(binding.storyImg)
                    pos = newPageIndex
                }
                override fun onFinished() {
                }
            }
            navController = findNavController()
        }

    }
    override fun onStart() {
        super.onStart()
        binding.spb.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}