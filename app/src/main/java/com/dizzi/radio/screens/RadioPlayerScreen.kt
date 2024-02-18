package com.dizzi.radio.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dizzi.radio.MainActivity
import com.dizzi.radio.R
import com.dizzi.radio.ui.theme.Matterhorn
import com.dizzi.radio.ui.theme.leagueSpartanFamily
import com.dizzi.radio.utils.CUSTOM_ACTION
import com.dizzi.radio.utils.MEDIA_PLAYER_MAX_VOLUME
import com.dizzi.radio.utils.service.MediaPlayerService
import com.dizzi.radio.utils.startMediaPlaybackService
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun RadioPlayerScreen(drawerState: DrawerState, appViewModel: AppViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isMediaPrepared = appViewModel.isMediaPrepared.observeAsState()
    val isPlaying = appViewModel.isPlaying.observeAsState()
    val listenerCount = appViewModel.listenerCount.observeAsState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Scaffold(
            topBar = {
                TopAppBar {
                    coroutineScope.launch {
                        if (drawerState.isClosed)
                            drawerState.open()
                        else
                            drawerState.close()
                    }
                }
            }
        ) { innerPadding ->

            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        modifier = Modifier.weight(1f),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.rectangle),
                        contentDescription = "",
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier,
                                color = Color.White,
                                fontSize = 40.sp,
                                text = appViewModel.titleName.value
                                    ?: stringResource(R.string.unknown),
                                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                color = Matterhorn,
                                fontSize = 18.sp,
                                text = appViewModel.artistName.value
                                    ?: stringResource(R.string.unknown),
                                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                painter = painterResource(id = R.drawable.headphone),
                                contentDescription = "",
                            )

                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                color = Color.White,
                                fontSize = 20.sp,
                                text = stringResource(id = R.string.listeners),
                                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier.padding(start = 2.dp),
                                color = Matterhorn,
                                fontSize = 20.sp,
                                text = (listenerCount.value ?: 0).toString(),
                                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        //Slider(appViewModel)

                        SliderVolume(appViewModel = appViewModel)


                        /*Slider(
                            value = (sliderPosition ?: position).toFloat(),
                            valueRange = 0f..(if (duration == C.TIME_UNSET) 0f else duration.toFloat()),
                            onValueChange = {
                                sliderPosition = it.toLong()
                            },
                            onValueChangeFinished = {
                                sliderPosition?.let {
                                    playerConnection.player.seekTo(it)
                                    position = it
                                }
                                sliderPosition = null
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.White
                            )
                        )*/
                    }
                }

                if (isMediaPrepared.value == true) {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .align(Alignment.Center)
                    ) {
                        Image(
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp)
                                .clickable {
                                    appViewModel.togglePlayPause()
                                    context.startMediaPlaybackService(
                                        artistName = appViewModel.artistName.value ?: context.getString(R.string.unknown),
                                        titleName = appViewModel.titleName.value ?: context.getString(R.string.unknown),
                                        isPlaying = appViewModel.isPlaying.value ?: true
                                    )
                                },
                            painter = if (isPlaying.value == true) painterResource(id = R.drawable.pause_icon) else painterResource(
                                id = R.drawable.play_icon
                            ),
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    }
}

private object RedRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            Color.Red,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            Color.Black,
            lightTheme = true
        )
}

@Composable
fun SliderVolume(appViewModel: AppViewModel) {
    val isMediaPrepared = appViewModel.isMediaPrepared.observeAsState()
    val sliderPosition = appViewModel.mediaPlayerVolume.observeAsState()
    Slider(
        value = sliderPosition.value ?: 0f,
        enabled = isMediaPrepared.value ?: false,
        valueRange = 0f..MEDIA_PLAYER_MAX_VOLUME,
        onValueChange = {
            appViewModel.onVolumeChanged(it)
        })
}

@Composable
fun Slider(appViewModel: AppViewModel) {
    val currentMinutes = appViewModel.currentPosition.observeAsState()
    val totalDuration = appViewModel.totalDuration.observeAsState()

    Column {
        Slider(
            value = currentMinutes.value?.toFloat() ?: 0f,
            onValueChange = {
                appViewModel.onSeekChanged(it.toInt())
            },
            valueRange = 0f..(totalDuration.value ?: 0).toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White
            )
        )
    }
}


@Composable
fun TopAppBar(onSettingIconClick: () -> Unit) {
    var formattedTime by remember { mutableStateOf("") }
    val sdf = remember { SimpleDateFormat("HH:mm", Locale.ROOT) }
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            formattedTime = sdf.format(Date())
            delay(60 * 1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(top = 24.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                color = Color.White,
                fontSize = 24.sp,
                text = stringResource(id = R.string.app_name_small),
                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Image(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = Color.Blue)
                    ) {
                        onSettingIconClick()
                    },
                painter = painterResource(id = R.drawable.nav_bar),
                contentDescription = "",
            )
        }

        Column(modifier = Modifier.padding(top = 30.dp)) {
            Text(
                modifier = Modifier,
                color = Color.White,
                fontSize = 40.sp,
                text = stringResource(id = R.string.now_playing),
                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White,
                fontSize = 15.sp,
                text = stringResource(id = R.string.enjoy_unlimited_music_sensation),
                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}