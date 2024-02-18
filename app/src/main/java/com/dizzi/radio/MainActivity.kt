package com.dizzi.radio

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.dizzi.radio.screens.AppViewModel
import com.dizzi.radio.screens.MainScreen
import com.dizzi.radio.ui.theme.DizzyRadioTheme
import com.dizzi.radio.utils.CUSTOM_ACTION
import com.dizzi.radio.utils.KEY_ACTION
import com.dizzi.radio.utils.service.MediaPlayerService
import com.dizzi.radio.utils.startMediaPlaybackService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startMediaPlaybackService(
                    artistName = viewModel.artistName.value ?: getString(R.string.unknown),
                    titleName = viewModel.titleName.value ?: getString(R.string.unknown),
                    isPlaying = viewModel.isPlaying.value ?: true
                )
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied.
            }
        }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            context ?: return
            if (intent.action == CUSTOM_ACTION) {
                val action = intent.getStringExtra(KEY_ACTION)
                when (action) {
                    MediaPlayerService.ACTION_TOGGLE_PLAY_PAUSE -> {
                        viewModel.togglePlayPause()

                        startMediaPlaybackService(
                            artistName = viewModel.artistName.value ?: getString(R.string.unknown),
                            titleName = viewModel.titleName.value ?: getString(R.string.unknown),
                            isPlaying = viewModel.isPlaying.value ?: true
                        )
                    }

                    MediaPlayerService.ACTION_VOLUME_UP -> {
                    }

                    MediaPlayerService.ACTION_VOLUME_DOWN -> {
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DizzyRadioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        CheckNotificationPermission()
                    }
                    MainScreen(appViewModel = viewModel)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(CUSTOM_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun CheckNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // make your action here
                startMediaPlaybackService(
                    artistName = viewModel.artistName.value ?: getString(R.string.unknown),
                    titleName = viewModel.titleName.value ?: getString(R.string.unknown),
                    isPlaying = viewModel.isPlaying.value ?: true
                )
            }

            shouldShowRequestPermissionRationale(permission) -> {
                AlertDialog(
                    onDismissRequest = { },
                    text = { Text(text = getString(R.string.notification_permission_required)) },
                    confirmButton = {
                        TextButton(onClick = {
                            val uri: Uri =
                                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                data = uri
                                startActivity(this)
                            }
                        }) { Text(text = getString(R.string.go_to_settings)) }
                    },
                )
            }

            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }


    class NotificationActionReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            val broadcastIntent = Intent(CUSTOM_ACTION)
            broadcastIntent.putExtra(KEY_ACTION, intent?.action)
            context.sendBroadcast(broadcastIntent)
        }
    }
}

