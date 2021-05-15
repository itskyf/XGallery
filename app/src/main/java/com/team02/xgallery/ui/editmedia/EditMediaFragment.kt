package com.team02.xgallery.ui.editmedia

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.team02.xgallery.databinding.FragmentEditImageBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.co.cyberagent.android.gpuimage.GPUImage
import java.util.*


@AndroidEntryPoint
class EditMediaFragment : Fragment() {
    private var _binding: FragmentEditImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditMediaViewModel by viewModels()
    val args: EditMediaFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditImageBinding.inflate(inflater, container, false)
        binding.mediaView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        binding.mediaView.setImage(args.mediaUri)

        binding.brightnessSlider.value = AdjustType.Brightness.default
        binding.contrastSlider.value = AdjustType.Contrast.default
        binding.hlSlider.value = AdjustType.Highlight.default
        binding.shadowSlider.value = AdjustType.Shadow.default
        binding.brightnessSlider.addOnChangeListener { _, value, _ ->
            binding.mediaView.filter = viewModel.onBrightnessChange(value)
            binding.mediaView.requestRender()
        }
        binding.contrastSlider.addOnChangeListener { _, value, _ ->
            binding.mediaView.filter = viewModel.onContrastChange(value)
            binding.mediaView.requestRender()
        }
        binding.hlSlider.addOnChangeListener { _, value, _ ->
            binding.mediaView.filter = viewModel.onHighlightChange(value)
            binding.mediaView.requestRender()
        }
        binding.shadowSlider.addOnChangeListener { _, value, _ ->
            binding.mediaView.filter = viewModel.onShadowChange(value)
            binding.mediaView.requestRender()
        }
        binding.brightnessReset.setOnClickListener {
            binding.brightnessSlider.value = AdjustType.Brightness.default
        }
        binding.contrastReset.setOnClickListener {
            binding.contrastSlider.value = AdjustType.Contrast.default
        }
        binding.hlReset.setOnClickListener {
            binding.hlSlider.value = AdjustType.Highlight.default
        }
        binding.shadowReset.setOnClickListener {
            binding.shadowSlider.value = AdjustType.Shadow.default
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveBtn.setOnClickListener {
            binding.mediaView.saveToPictures(
                "XGallery", "${UUID.randomUUID()}.png", null
            )
            findNavController().popBackStack()
        }
        viewModel.filters.forEach {
            val tab = binding.tabLayout.newTab()
            tab.text = it.name
            binding.tabLayout.addTab(tab)
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.mediaView.filter = viewModel.onFilterChange(it)
                    binding.mediaView.requestRender()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}