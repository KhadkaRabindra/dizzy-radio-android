package com.dizzi.radio.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dizzi.radio.R
import com.dizzi.radio.screens.common.CommonInputField
import com.dizzi.radio.ui.theme.leagueSpartanFamily
import kotlinx.coroutines.launch

@Composable
fun RequestASongScreen(drawerState: DrawerState) {
    var artistName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }

    val videoIDErrorMessage = remember { mutableStateOf("") }
    val titleErrorMessage = remember { mutableStateOf("") }
    val imageURLErrorMessage = remember { mutableStateOf("") }
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Scaffold(
            topBar = {
                DefaultTopAppBar {
                    coroutineScope.launch {
                        if (drawerState.isClosed)
                            drawerState.open()
                        else
                            drawerState.close()
                    }
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(30.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        color = Color.White,
                        fontSize = 15.sp,
                        text = stringResource(id = R.string.about),
                        fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(top = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                CommonInputField(title = stringResource(id = R.string.artist_name),
                                    placeHolder = stringResource(id = R.string.please_provide_artist_name),
                                    value = artistName,
                                    onTextChanged = {
                                        artistName = it
                                    },
                                    content = {
                                        if (videoIDErrorMessage.value.isNotEmpty()) {
                                            Text(
                                                fontFamily = leagueSpartanFamily,
                                                fontWeight = FontWeight.Normal,
                                                text = videoIDErrorMessage.value,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                modifier = Modifier.paddingFromBaseline(bottom = 15.dp)
                                            )
                                        }
                                    })


                                CommonInputField(title = stringResource(id = R.string.title),
                                    placeHolder = stringResource(id = R.string.please_enter_title),
                                    value = title,
                                    onTextChanged = {
                                        title = it
                                    },
                                    content = {
                                        if (titleErrorMessage.value.isNotEmpty()) {
                                            Text(
                                                fontFamily = leagueSpartanFamily,
                                                fontWeight = FontWeight.Normal,
                                                text = titleErrorMessage.value,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                modifier = Modifier.paddingFromBaseline(bottom = 15.dp)
                                            )
                                        }
                                    })


                                CommonInputField(title = stringResource(id = R.string.genre),
                                    placeHolder = stringResource(id = R.string.please_enter_genre),
                                    value = genre,
                                    onTextChanged = {
                                        genre = it
                                    },
                                    content = {
                                        if (imageURLErrorMessage.value.isNotEmpty()) {
                                            Text(
                                                fontFamily = leagueSpartanFamily,
                                                fontWeight = FontWeight.Normal,
                                                text = imageURLErrorMessage.value,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                modifier = Modifier.paddingFromBaseline(bottom = 15.dp)
                                            )
                                        }
                                    })


                                Button(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(53.dp), onClick = {
                                    videoIDErrorMessage.value = ""
                                    titleErrorMessage.value = ""
                                    imageURLErrorMessage.value = ""
                                    val isValid = context.validateInput(
                                        artistName = artistName,
                                        videoIDErrorMessage = videoIDErrorMessage,
                                        title = title,
                                        titleErrorMessage = titleErrorMessage,
                                        genre = genre,
                                        genreErrorMessage = imageURLErrorMessage
                                    )
                                    if (isValid) {

                                    }
                                }) {
                                    Text(text = "Validate")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun Context.validateInput(
    artistName: String,
    title: String,
    genre: String,
    videoIDErrorMessage: MutableState<String>,
    titleErrorMessage: MutableState<String>,
    genreErrorMessage: MutableState<String>,
): Boolean {
    var isValid = true

    if (artistName.isEmpty()) {
        videoIDErrorMessage.value = getString(R.string.please_enter_artist_name)
        isValid = false
    }

    if (title.isEmpty()) {
        titleErrorMessage.value = getString(R.string.please_enter_title)
        isValid = false
    }

    if (genre.isEmpty()) {
        genreErrorMessage.value = getString(R.string.please_enter_genre)
        isValid = false
    }

    return isValid
}