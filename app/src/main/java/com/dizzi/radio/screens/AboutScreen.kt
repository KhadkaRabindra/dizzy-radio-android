package com.dizzi.radio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dizzi.radio.R
import com.dizzi.radio.ui.theme.leagueSpartanFamily

@Composable
fun AboutScreen() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp),
                color = Color.White,
                fontSize = 15.sp,
                text = stringResource(id = R.string.about),
                fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}