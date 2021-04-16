package com.team02.xgallery.ui.story

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.team02.xgallery.databinding.FragmentStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import pt.tornelas.segmentedprogressbar.SegmentedProgressBarListener

@AndroidEntryPoint
class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var listImage = listOf<Uri>()
    private var pos = 0

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
        binding.spb.segmentCount = 5
        binding.spb.start()
        binding.storyImg.load(listImage[pos])
        binding.leftBtn.setOnClickListener{
            if(pos==0)
            {
                binding.spb.restartSegment()
            }else {
                binding.spb.previous()
                pos--
                binding.storyImg.load(listImage[pos])
            }
        }
        binding.rightBtn.setOnClickListener{
            binding.spb.next()
            pos++
            binding.storyImg.load(listImage[pos])
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
                pos++
                binding.storyImg.load(listImage[pos])
            }
            override fun onFinished() {
            }
        }
        navController = findNavController()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}