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
package com.mobnetic.newtonstimer.balls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobnetic.newtonstimer.configuration.ConfigurationHint
import com.mobnetic.newtonstimer.configuration.configurationDragModifier
import com.mobnetic.newtonstimer.timer.TimerState
import com.mobnetic.newtonstimer.timer.TimerViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive

@Composable
fun SwingingBalls(modifier: Modifier = Modifier) {
    Column(modifier) {
        val viewModel: TimerViewModel = viewModel()
        val angles = animateAngles(viewModel)

        val isConfigured = viewModel.state is TimerState.Configured
        val otherBallsAlpha by animateFloatAsState(if (isConfigured) 1f else CONFIGURATION_OTHER_BALLS_ALPHA)

        var ballSize by remember { mutableStateOf(BallSize()) }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val ballWidth = constraints.maxWidth / angles.size
            ballSize = BallSize(ballWidth / 2, constraints.maxHeight)

            if (!isConfigured) {
                ConfigurationHint(angles.first(), ballSize)
            }
            BallsOnStrings(viewModel, isConfigured, angles, ballSize, otherBallsAlpha)
        }
        Shadows(angles, ballSize, otherBallsAlpha)
    }
}

@Composable
private fun BallsOnStrings(
    viewModel: TimerViewModel,
    isConfigured: Boolean,
    angles: FloatArray,
    ballSize: BallSize,
    otherBallsAlpha: Float
) {
    Row(Modifier.fillMaxSize()) {
        val draggable = if (!isConfigured) {
            Modifier.configurationDragModifier(ballSize, onAngleChanged = viewModel::configureAngle, onDragEnd = viewModel::play)
        } else Modifier

        BallOnString(angles.first(), draggable)
        angles.forOtherAngles { angle ->
            BallOnString(angle, Modifier.alpha(otherBallsAlpha))
        }
    }
}

@Composable
private fun Shadows(angles: FloatArray, ballSize: BallSize, otherBallsAlpha: Float) {
    Row(Modifier.fillMaxWidth()) {
        Shadow(angles.first(), ballSize)
        angles.forOtherAngles { angle ->
            Shadow(angle, ballSize, Modifier.alpha(otherBallsAlpha))
        }
    }
}

private inline fun FloatArray.forOtherAngles(action: (angle: Float) -> Unit) {
    (1..lastIndex).forEach { index -> action(get(index)) }
}

@Composable
private fun animateAngles(viewModel: TimerViewModel): FloatArray {
    val state = viewModel.state as? TimerState.Configured.Running ?: return viewModel.getAnimationAngles()

    var angles by remember { mutableStateOf(viewModel.getAnimationAngles()) }
    LaunchedEffect(state) {
        do {
            angles = viewModel.getAnimationAngles()
            awaitFrame()
        } while (isActive)
    }
    return angles
}

private const val CONFIGURATION_OTHER_BALLS_ALPHA = 0.15f
