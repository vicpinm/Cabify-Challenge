package com.vicpin.cabifychallenge

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior


fun <T : View> BottomSheetBehavior<T>.toggle() {
    state = if (state == BottomSheetBehavior.STATE_EXPANDED) {
        BottomSheetBehavior.STATE_COLLAPSED
    } else {
        BottomSheetBehavior.STATE_EXPANDED
    }
}

fun <T : View> BottomSheetBehavior<T>.setExpandStateListener(onStatusChanged: (Boolean) -> Unit) {
    setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(view: View, p1: Float) {}

        override fun onStateChanged(view: View, state: Int) {
            if (state == BottomSheetBehavior.STATE_EXPANDED) {
                onStatusChanged(true)
            } else {
                onStatusChanged(false)
            }
        }
    })
}

fun <T> Fragment.observe(data: LiveData<T>, listener: (T) -> Unit) {
    data.observe(viewLifecycleOwner, Observer<T> {
        listener(it)
    })
}


