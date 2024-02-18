package com.dizzi.radio.screens

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dizzi.radio.data.repository.RadioRepositoryImpl
import com.dizzi.radio.utils.AUDIO_URL
import com.dizzi.radio.utils.MEDIA_PLAYER_MAX_VOLUME
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: RadioRepositoryImpl
) : ViewModel() {

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _isMediaPrepared = MutableLiveData(false)
    val isMediaPrepared: LiveData<Boolean> = _isMediaPrepared

    private val _currentPosition = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition

    private val _mediaPlayerVolume = MutableLiveData(MEDIA_PLAYER_MAX_VOLUME)
    val mediaPlayerVolume = _mediaPlayerVolume

    private val _totalDuration = MutableLiveData(0)
    val totalDuration: LiveData<Int> = _totalDuration

    private val _artistName = MutableLiveData("")
    val artistName: LiveData<String> = _artistName

    private val _titleName = MutableLiveData("")
    val titleName: LiveData<String> = _titleName

    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private var timer: Timer? = null
    private var mySeekTimerTask: MySeekTimerTask? = null


    private val _listenerCount = MutableLiveData<Int>()
    val listenerCount: LiveData<Int> get() = _listenerCount


    inner class MySeekTimerTask : TimerTask() {
        override fun run() {
            _currentPosition.postValue(mediaPlayer?.currentPosition)
        }
    }

    init {
        setUpForMediaPlayer()
        setUpMediaRetriever()
        onVolumeChanged(_mediaPlayerVolume.value ?: 0f)
        refreshListenerCount()
    }

    private fun refreshListenerCount() {
        repository.getListenerCount { count ->
            _listenerCount.postValue(if (count >= 0) count else 0)
            if (count < 0){
                resetListenerCount()
            }
        }
    }

    private fun setUpMediaRetriever() {
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(AUDIO_URL)
        _artistName.value =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        _titleName.value = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
    }

    private fun setUpForMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
        )
        try {
            mediaPlayer?.setOnPreparedListener { mp ->
                _isMediaPrepared.value = true
                resumeMediaPlayer()
            }
            mediaPlayer?.setDataSource(AUDIO_URL)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                decreaseListenerCount()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _totalDuration.value = mediaPlayer?.duration
    }

    fun togglePlayPause() {
        if (_isPlaying.value == true) {
            pauseMediaPlayer()
        } else {
            resumeMediaPlayer()
        }
    }

    private fun resumeMediaPlayer() {
        mediaPlayer?.start()
        if (timer != null) {
            timer?.cancel()
        }
        timer = Timer()
        mySeekTimerTask = MySeekTimerTask()
        timer?.schedule(mySeekTimerTask, 0, 1000)
        _isPlaying.value = true
        increaseListenerCount()
    }

    private fun pauseMediaPlayer() {
        mediaPlayer?.pause()
        _isPlaying.value = false
        decreaseListenerCount()
    }

    private fun decreaseListenerCount() {
        repository.updateListenerCount(_listenerCount.value?.minus(1) ?: 0)
    }

    private fun increaseListenerCount() {
        repository.updateListenerCount(_listenerCount.value?.plus(1) ?: 0)
    }

    private fun resetListenerCount() {
        repository.updateListenerCount(0)
    }

    fun onSeekChanged(newPosition: Int) {
        mediaPlayer?.seekTo(newPosition)
        _currentPosition.value = newPosition
    }

    fun onVolumeChanged(progress: Float) {
        val volume = (1 - Math.log(MEDIA_PLAYER_MAX_VOLUME - progress.toDouble()) / Math.log(
            MEDIA_PLAYER_MAX_VOLUME.toDouble()
        )).toFloat()
        /*val volume = (Math.log(MEDIA_PLAYER_MAX_VOLUME - progress.toDouble()) / Math.log(
            MEDIA_PLAYER_MAX_VOLUME.toDouble()
        )).toFloat()*/
        mediaPlayer?.setVolume(volume, volume)
        _mediaPlayerVolume.value = progress
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        try {
            timer?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _isPlaying.value = false
        _isMediaPrepared.value = false
    }
}