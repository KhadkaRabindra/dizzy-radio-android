package com.dizzi.radio.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dizzi.radio.ui.theme.leagueSpartanFamily

@Composable
fun CommonInputField(
    title: String,
    placeHolder: String,
    value: String, onTextChanged: (String) -> Unit, bottomPadding: Int = 15,
    content: @Composable () -> Unit, isError: Boolean = false,

    ) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(bottom = bottomPadding.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontFamily = leagueSpartanFamily, fontWeight = FontWeight.Normal,
            modifier = Modifier.paddingFromBaseline(bottom = 10.dp),
        )
        OutlinedTextField(
            colors =  TextFieldDefaults.colors(Color.White),
            value = value,
            onValueChange = onTextChanged,
            placeholder = { Text(text = placeHolder) },
            shape = RoundedCornerShape(10),
            isError = isError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {keyboardController?.hide()}),
            modifier = Modifier.fillMaxWidth()
        )
        content()
    }
}