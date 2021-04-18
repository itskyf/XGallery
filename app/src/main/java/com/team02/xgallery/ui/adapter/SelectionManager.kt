package com.team02.xgallery.ui.adapter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectionManager {
    private var selectedItemKeySet = mutableSetOf<Any>()
    private val _selectedCount = MutableStateFlow<Int>(0)
    val selectedCount: StateFlow<Int> = _selectedCount
    private val _onSelectionMode = MutableStateFlow<Boolean>(false)
    val onSelectionMode: StateFlow<Boolean> = _onSelectionMode

    fun getItemKeyList(): List<Any> {
        return selectedItemKeySet.toList()
    }

    fun isSelected(itemKey: Any): Boolean {
        return selectedItemKeySet.contains(itemKey)
    }

    fun select(itemKey: Any) {
        if (selectedItemKeySet.contains(itemKey)) {
            selectedItemKeySet.remove(itemKey)
            if (selectedItemKeySet.isEmpty()) {
                _onSelectionMode.value = false
            }
        } else {
            if (selectedItemKeySet.isEmpty()) {
                _onSelectionMode.value = true
            }
            selectedItemKeySet.add(itemKey)
        }
        _selectedCount.value = selectedItemKeySet.size
    }


    fun clear() {
        selectedItemKeySet.clear()
        _selectedCount.value = selectedItemKeySet.size
        _onSelectionMode.value = false
    }
}