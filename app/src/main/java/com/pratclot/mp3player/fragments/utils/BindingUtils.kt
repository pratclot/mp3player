package com.pratclot.mp3player.fragments.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pratclot.mp3player.viewmodels.Audio

@BindingAdapter("setContent")
fun TextView.setContent(audio: Audio) {
    text = audio.contentUri
}

@BindingAdapter("setName")
fun TextView.setName(audio: Audio) {
    text = audio.name
}
