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

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.mobnetic.newtonstimer.configuration.ConfigurationHint
import com.mobnetic.newtonstimer.configuration.configurationDragModifier
import com.mobnetic.newtonstimer.timer.NewtonsTimerViewModel
import com.mobnetic.newtonstimer.timer.TimerState
import kotlinx.coroutines.isActive

@Composable
fun SwingingBallsContainer(
    viewModel: NewtonsTimerViewModel,
    ballsInnerRatio: Float,
    configurationTransitionAnimSpec: FiniteAnimationSpec<Float>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val translationX by animateFloatAsState(
            targetValue = if (viewModel.isConfigured) 0f else constraints.maxWidth / 2f,
            animationSpec = configurationTransitionAnimSpec
        )
        SwingingBalls(
            viewModel,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(ballsInnerRatio)
                .graphicsLayer { this.translationX = translationX }
        )
    }
}

@Composable
private fun SwingingBalls(
    viewModel: NewtonsTimerViewModel,
    modifier: Modifier,
) {
    Column(modifier) {
        val angles = animateAnglesAsState(viewModel)

        val isConfigured = viewModel.isConfigured
        val otherBallsAlpha =
            animateFloatAsState(if (isConfigured) 1f else CONFIGURATION_OTHER_BALLS_ALPHA)

        var ballSize by remember { mutableStateOf(BallSize()) }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val ballWidth = constraints.maxWidth / angles.size
            ballSize = BallSize(ballWidth / 2, constraints.maxHeight)

            ConfigurationHint(isConfigured, angles.first(), ballSize)

            BallsOnStrings(
                isConfigured = isConfigured,
                angles = angles,
                ballSize = ballSize,
                otherBallsAlpha = otherBallsAlpha::value,
                onConfigurationAngleChanged = viewModel::configureStartAngle,
                onDragEnd = viewModel::play
            )
        }
        Shadows(
            angles = angles,
            ballSize = ballSize,
            otherBallsAlpha = otherBallsAlpha::value,
        )
    }
}

@Composable
private fun BallsOnStrings(
    isConfigured: Boolean,
    angles: List<Float>,
    ballSize: BallSize,
    otherBallsAlpha: () -> Float,
    onConfigurationAngleChanged: (Float) -> Unit,
    onDragEnd: () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        val draggable = if (!isConfigured) Modifier.configurationDragModifier(
            ballSize,
            onConfigurationAngleChanged,
            onDragEnd
        ) else Modifier

        BallOnString(
            angle = angles.first(),
            modifier = draggable
        )

        angles.forOtherAngles { angle ->
            BallOnString(
                angle = angle,
                modifier = Modifier.graphicsLayer { alpha = otherBallsAlpha() }
            )
        }
    }
}

@Composable
private fun Shadows(
    angles: List<Float>,
    ballSize: BallSize,
    otherBallsAlpha: () -> Float,
) {
    Row(Modifier.fillMaxWidth()) {
        Shadow(angles.first(), ballSize)
        angles.forOtherAngles { angle ->
            Shadow(
                angle = angle,
                ballSize = ballSize,
                alpha = otherBallsAlpha
            )
        }
    }
}

@Composable
private fun animateAnglesAsState(viewModel: NewtonsTimerViewModel): List<Float> {
    val angles by remember { mutableStateOf(viewModel.angles) }

    val state = viewModel.state
    if (state is TimerState.Configured.Running) {
        LaunchedEffect(state) {
            while (isActive) {
                withFrameMillis {
                    viewModel.refreshAngles()
                }
            }
        }
    } else {
        viewModel.refreshAngles()
    }
    return angles
}

private inline fun List<Float>.forOtherAngles(action: (angle: Float) -> Unit) {
    (1..lastIndex).forEach { index -> action(get(index)) }
}

private const val CONFIGURATION_OTHER_BALLS_ALPHA = 0.25f
