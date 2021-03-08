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
package com.mobnetic.newtonstimer.timer

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobnetic.newtonstimer.R

@Composable
fun ButtonsBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val viewModel: TimerViewModel = viewModel()

        DarkModeToggleButton(
            darkMode = viewModel.darkMode,
            onDarkModeChanged = { viewModel.darkMode = it },
            modifier = Modifier.weight(1f)
        )

        val playPauseButtonSize = Modifier.size(72.dp)
        if (viewModel.state is TimerState.Configured) {
            PlayPauseButton(
                isRunning = viewModel.state is TimerState.Configured.Running,
                pause = viewModel::pause,
                play = viewModel::play,
                modifier = playPauseButtonSize
            )
        } else {
            Spacer(playPauseButtonSize)
        }

        if (viewModel.state is TimerState.Configured.Paused) {
            ResetButton(onReset = viewModel::reset, Modifier.weight(1f))
        } else {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun DarkModeToggleButton(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    IconButton(onClick = { onDarkModeChanged(!darkMode) }, modifier) {
        val color by animateColorAsState(targetValue = MaterialTheme.colors.onSurface)
        val modifier = Modifier.size(ICONS_SIZE)
        when (darkMode) {
            true -> Icon(Icons.Default.LightMode, stringResource(R.string.light_mode), modifier, color)
            else -> Icon(Icons.Default.DarkMode, stringResource(R.string.dark_mode), modifier, color)
        }
    }
}

@Composable
private fun PlayPauseButton(
    isRunning: Boolean,
    pause: () -> Unit,
    play: () -> Unit,
    modifier: Modifier
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
        val modifier = Modifier.padding(12.dp)
        when (isRunning) {
            true -> Icon(Icons.Default.Pause, stringResource(R.string.pause), modifier)
            else -> Icon(Icons.Default.PlayArrow, stringResource(R.string.play), modifier)
        }
    }
}

@Composable
private fun ResetButton(onReset: () -> Unit, modifier: Modifier) {
    IconButton(onClick = onReset, modifier) {
        val color by animateColorAsState(targetValue = MaterialTheme.colors.onSurface)
        val modifier = Modifier.size(ICONS_SIZE)
        Icon(Icons.Default.Close, stringResource(R.string.reset), modifier, color)
    }
}

private val ICONS_SIZE = 36.dp
