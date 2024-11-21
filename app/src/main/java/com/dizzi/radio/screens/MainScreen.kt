package com.dizzi.radio.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.rememberNavController
import com.dizzi.radio.R
import com.dizzi.radio.nav.AppNavigation
import com.dizzi.radio.nav.listOfNavItems
import com.dizzi.radio.screens.common.ComposableLifecycle
import com.dizzi.radio.ui.theme.leagueSpartanFamily
import com.dizzi.radio.utils.startMediaPlaybackService
import kotlinx.coroutines.launch


@Composable
fun MainScreen(appViewModel: AppViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    val context = LocalContext.current

    ComposableLifecycle { source, event ->
        if (event == Lifecycle.Event.ON_DESTROY || event == Lifecycle.Event.ON_CREATE) {
            if (appViewModel.isPlaying.value == true){
                appViewModel.togglePlayPause()
                context.startMediaPlaybackService(
                    artistName = appViewModel.artistName.value ?: context.getString(R.string.unknown),
                    titleName = appViewModel.titleName.value ?: context.getString(R.string.unknown),
                    isPlaying = appViewModel.isPlaying.value ?: true
                )
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        ModalNavigationDrawer(
            {
                ModalDrawerSheet(drawerShape = RectangleShape) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight()
                                .background(color = Color.Black)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(100.dp)
                                    .background(color = Color.Black)
                            ) {
                                Row(
                                    modifier = Modifier.padding(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .padding(top = 10.dp)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple(color = Color.Blue)
                                            ) {
                                                scope.launch {
                                                    if (drawerState.isClosed)
                                                        drawerState.open()
                                                    else
                                                        drawerState.close()
                                                }

                                            },
                                        painter = painterResource(id = R.drawable.close),
                                        contentDescription = "",
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyColumn {
                                items(listOfNavItems) {
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 40.dp)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple(color = Color.Blue)
                                            ) {
                                                navController.navigate(it.route)
                                                scope.launch {
                                                    if (drawerState.isClosed)
                                                        drawerState.open()
                                                    else
                                                        drawerState.close()
                                                }

                                            },
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically

                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(20.dp),
                                            painter = painterResource(id = it.icon),
                                            contentDescription = "",
                                        )

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(16.dp),
                                        ) {

                                            Text(
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                text = stringResource(id = it.title),
                                                fontFamily = leagueSpartanFamily,
                                                fontWeight = FontWeight.Normal,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }, drawerState = drawerState
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                AppNavigation(
                    navController = navController,
                    drawerState = drawerState,
                    appViewModel = appViewModel
                )
            }
        }
    }
}
