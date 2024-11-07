package com.marklynch.steamdeck.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TextEntryDialog(onDismissRequest: () -> Unit,
    startText: String,
    onValueChange: (String) -> Unit) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {focusRequester.requestFocus()}

    val state = remember {mutableStateOf(TextFieldValue(startText))}

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
                .background(Color.DarkGray)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        val text = state.value.text
                        state.value = state.value.copy(
                            selection = TextRange(0, text.length)
                        )
                    }
                },
        ) {
            TextField(
                value = state.value,
                onValueChange = { newText ->
                    onValueChange(newText.text)
                    state.value = newText},
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDismissRequest()
                    }
                )
            )
        }
    }
}