package com.dizzi.radio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dizzi.radio.R
import com.dizzi.radio.ui.theme.leagueSpartanFamily
import kotlinx.coroutines.launch

@Composable
fun TermsAndConditionScreen(drawerState: DrawerState) {

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
                        text = stringResource(id = R.string.terms_and_conditions),
                        fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        text = stringResource(id = R.string.terms_and_conditions_description),
                        fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}