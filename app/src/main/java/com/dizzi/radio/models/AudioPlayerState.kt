package com.dizzi.radio.models

import android.media.MediaPlayer

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val mediaPlayer: MediaPlayer? = null
)