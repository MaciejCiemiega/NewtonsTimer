/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobnetic.newtonstimer.unused.keypad

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobnetic.newtonstimer.unused.keypad.KeypadAction.Backspace
import com.mobnetic.newtonstimer.unused.keypad.KeypadAction.Number
import com.mobnetic.newtonstimer.timer.TimerViewModel

@Preview(heightDp = 700, widthDp = 400)
@Composable
fun Keypad(modifier: Modifier = Modifier) {
    val viewModel: TimerViewModel = viewModel()
    fun action(action: KeypadAction): () -> Unit = { /*viewModel.handleKeyboardAction(action)*/ }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.weight(1f)) {
            KeyboardButton("1", action(Number.Key1))
            Space()
            KeyboardButton("2", action(Number.Key2))
            Space()
            KeyboardButton("3", action(Number.Key3))
        }
        Space()
        Row(Modifier.weight(1f)) {
            KeyboardButton("4", action(Number.Key4))
            Space()
            KeyboardButton("5", action(Number.Key5))
            Space()
            KeyboardButton("6", action(Number.Key6))
        }
        Space()
        Row(Modifier.weight(1f)) {
            KeyboardButton("7", action(Number.Key7))
            Space()
            KeyboardButton("8", action(Number.Key8))
            Space()
            KeyboardButton("9", action(Number.Key9))
        }
        Space()
        Row(Modifier.weight(1f)) {
            Spacer(Modifier.weight(1f))
            Space()
            KeyboardButton("0", action(Number.Key0))
            Space()
            KeyboardButton("<", action(Backspace))
        }
    }
}

@Composable
private fun Space() {
    Spacer(Modifier.size(16.dp))
}

@Composable
private fun RowScope.KeyboardButton(text: String, onClick: () -> Unit) {
    val color by animateColorAsState(MaterialTheme.colors.primary)
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        border = null,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = color
        ),
    ) {
        Text(
            text, fontSize = 32.sp,
            fontWeight = FontWeight.Light
        )
    }
}
