package com.dizzi.radio.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.dizzi.radio.utils.service.MediaPlayerService

fun Context.startMediaPlaybackService(
    artistName: String,
    titleName: String,
    isPlaying: Boolean
) {
    val intent = Intent(this, MediaPlayerService::class.java)
    intent.putExtra(MediaPlayerService.KEY_ARTIST_NAME, artistName)
    intent.putExtra(MediaPlayerService.KEY_TITLE_NAME, titleName)
    intent.putExtra(MediaPlayerService.KEY_IS_PLAYING, isPlaying)
    ContextCompat.startForegroundService(this, intent)
}