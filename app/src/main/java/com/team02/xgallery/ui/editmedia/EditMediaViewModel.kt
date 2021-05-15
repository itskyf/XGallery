package com.team02.xgallery.ui.editmedia

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.cyberagent.android.gpuimage.filter.*
import javax.inject.Inject

@HiltViewModel
class EditMediaViewModel @Inject constructor() : ViewModel() {
    private val brightnessFilter =
        GPUImageBrightnessFilter(AdjustType.Brightness.default)
    private val contrastFilter = GPUImageContrastFilter(AdjustType.Contrast.default)
    private val highlightShadowFilter =
        GPUImageHighlightShadowFilter(AdjustType.Shadow.default, AdjustType.Highlight.default)
    private var currentFilterPos = 0
    private val adjustGroup
        get() = GPUImageFilterGroup(
            listOf(filters[currentFilterPos].filter) + listOf(
                brightnessFilter,
                contrastFilter,
                highlightShadowFilter
            )
        )

    val filters = listOf(
        FilterType("Normal", GPUImageFilter()),
        FilterType("Grayscale", GPUImageGrayscaleFilter()),
        FilterType("Sobel", GPUImageSobelEdgeDetectionFilter()),
        FilterType("Sketch", GPUImageSketchFilter()),
        FilterType("Invert", GPUImageColorInvertFilter()),
    )

    fun onFilterChange(pos: Int): GPUImageFilter {
        currentFilterPos = pos
        return adjustGroup
    }

    fun onBrightnessChange(value: Float): GPUImageFilter {
        brightnessFilter.setBrightness(value)
        return adjustGroup
    }

    fun onContrastChange(value: Float): GPUImageFilter {
        contrastFilter.setContrast(value)
        return adjustGroup
    }

    fun onHighlightChange(value: Float): GPUImageFilter {
        highlightShadowFilter.setHighlights(value)
        return adjustGroup
    }

    fun onShadowChange(value: Float): GPUImageFilter {
        highlightShadowFilter.setShadows(value)
        return adjustGroup
    }
}