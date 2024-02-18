package com.dizzi.radio.utils.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dizzi.radio.MainActivity
import com.dizzi.radio.R

class MediaPlayerService : Service() {
    private var artistName = ""
    private var titleName = ""
    private var isPlaying = true

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        artistName = intent?.getStringExtra(KEY_ARTIST_NAME) ?: getString(R.string.unknown)
        titleName = intent?.getStringExtra(KEY_TITLE_NAME) ?: getString(R.string.unknown)
        isPlaying = intent?.getBooleanExtra(KEY_IS_PLAYING, true) ?: true
        startForeground()
        return START_NOT_STICKY
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationLayout = RemoteViews(packageName, R.layout.notification_layout)
        notificationLayout.setTextViewText(R.id.titleName, titleName)
        notificationLayout.setTextViewText(R.id.artistName, artistName)

        if (isPlaying){
            notificationLayout.setImageViewResource(R.id.playPauseButton, R.drawable.pause_icon)
        }else{
            notificationLayout.setImageViewResource(R.id.playPauseButton, R.drawable.play_icon)
        }
        notificationLayout.setOnClickPendingIntent(
            R.id.playPauseButton,
            getPendingIntentForAction(ACTION_TOGGLE_PLAY_PAUSE)
        )
        /*notificationLayout.setOnClickPendingIntent(R.id.volumeUpButton, getPendingIntentForAction(ACTION_VOLUME_UP))
        notificationLayout.setOnClickPendingIntent(R.id.volumeDownButton, getPendingIntentForAction(ACTION_VOLUME_DOWN))*/


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationBuilder.foregroundServiceBehavior =
                Notification.FOREGROUND_SERVICE_IMMEDIATE
        }
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setCustomContentView(notificationLayout)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_CHANNEL_ID, notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(
                    NOTIFICATION_CHANNEL_ID,
                    notification
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    private fun getPendingIntentForAction(action: String): PendingIntent {
        val intent = Intent(this, MainActivity.NotificationActionReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    companion object {
        const val ACTION_TOGGLE_PLAY_PAUSE = "action_toggle_play_pause"
        const val ACTION_VOLUME_UP = "action_volume_up"
        const val ACTION_VOLUME_DOWN = "action_volume_down"
        const val KEY_TITLE_NAME = "key_title_name"
        const val KEY_ARTIST_NAME = "key_artist_name"
        const val KEY_IS_PLAYING = "key_is_playing"
        const val NOTIFICATION_CHANNEL_ID = 1
    }
}

