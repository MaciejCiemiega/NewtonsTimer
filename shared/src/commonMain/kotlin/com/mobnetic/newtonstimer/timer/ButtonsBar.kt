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
@file:Suppress("PrivatePropertyName")

package com.mobnetic.newtonstimer.timer

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobnetic.newtonstimer.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ButtonsBar(
    viewModel: NewtonsTimerViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state

    Row(
        modifier = modifier.height(BUTTONS_BAR_HEIGHT),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DarkModeToggleButton(
            darkMode = viewModel.darkMode,
            onDarkModeChanged = { viewModel.darkMode = it },
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier.size(BUTTONS_BAR_HEIGHT)
        ) {
            if (state is TimerState.Configured) {
                PlayPauseButton(
                    isRunning = state is TimerState.Configured.Running,
                    pause = viewModel::pause,
                    play = viewModel::play,
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (state is TimerState.Configured.Paused) {
                ResetButton(onReset = viewModel::reset)
            }
        }
    }
}

@Composable
private fun DarkModeToggleButton(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = { onDarkModeChanged(!darkMode) },
        modifier = modifier
    ) {
        val color by animateColorAsState(targetValue = MaterialTheme.colors.onSurface)
        if (darkMode) {
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = stringResource(MR.strings.light_mode_a11y),
                modifier = Modifier.size(ICONS_SIZE),
                tint = color
            )
        } else {
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = stringResource(MR.strings.dark_mode_a11y),
                modifier = Modifier.size(ICONS_SIZE),
                tint = color
            )
        }
    }
}

@Composable
private fun PlayPauseButton(
    isRunning: Boolean,
    pause: () -> Unit,
    play: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(MaterialTheme.colors.primary)
    OutlinedButton(
        onClick = { if (isRunning) pause() else play() },
        modifier = modifier.aspectRatio(1f),
        shape = CircleShape,
        border = BorderStroke(1.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = color
        ),
        contentPadding = PaddingValues()
    ) {
        if (isRunning) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = stringResource(MR.strings.pause_a11y),
                modifier = Modifier.size(ICONS_SIZE)
            )
        } else {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(MR.strings.play_a11y),
                modifier = Modifier.size(ICONS_SIZE)
            )
        }
    }
}

@Composable
private fun ResetButton(
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onReset,
        modifier = modifier
    ) {
        val color by animateColorAsState(targetValue = MaterialTheme.colors.onSurface)
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(MR.strings.reset_a11y),
            modifier = Modifier.size(ICONS_SIZE),
            tint = color
        )
    }
}

private val BUTTONS_BAR_HEIGHT = 72.dp
private val ICONS_SIZE = 36.dp
