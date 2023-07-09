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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.mobnetic.newtonstimer.TestTags
import com.mobnetic.newtonstimer.configuration.ConfigurationHint
import com.mobnetic.newtonstimer.configuration.configurationDragModifier
import com.mobnetic.newtonstimer.timer.TimerState
import com.mobnetic.newtonstimer.timer.TimerViewModel
import kotlinx.coroutines.isActive

@Composable
fun SwingingBallsContainer(viewModel: TimerViewModel, ballsInnerRatio: Float, modifier: Modifier) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val translationX by animateFloatAsState(targetValue = if (viewModel.isConfigured) 0f else constraints.maxWidth / 2f)
        SwingingBalls(
            viewModel,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(ballsInnerRatio)
                .graphicsLayer(translationX = translationX)
        )
    }
}

@Composable
private fun SwingingBalls(viewModel: TimerViewModel, modifier: Modifier) {
    Column(modifier) {
        val angles = animateAnglesAsState(viewModel)

        val isConfigured = viewModel.isConfigured
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
            BallsOnStrings(
                isConfigured,
                angles,
                ballSize,
                otherBallsAlpha,
                onConfigurationAngleChanged = viewModel::configureStartAngle,
                onDragEnd = viewModel::play
            )
        }
        Shadows(angles, ballSize, otherBallsAlpha)
    }
}

@Composable
private fun BallsOnStrings(
    isConfigured: Boolean,
    angles: List<Float>,
    ballSize: BallSize,
    otherBallsAlpha: Float,
    onConfigurationAngleChanged: (Float) -> Unit,
    onDragEnd: () -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        val draggable = if (!isConfigured) Modifier.configurationDragModifier(
            ballSize,
            onConfigurationAngleChanged,
            onDragEnd
        ) else Modifier

        BallOnString(angles.first(), draggable.testTag(TestTags.draggableBall))
        angles.forOtherAngles { angle ->
            BallOnString(angle, Modifier.alpha(otherBallsAlpha))
        }
    }
}

@Composable
private fun Shadows(angles: List<Float>, ballSize: BallSize, otherBallsAlpha: Float) {
    Row(Modifier.fillMaxWidth()) {
        Shadow(angles.first(), ballSize)
        angles.forOtherAngles { angle ->
            Shadow(angle, ballSize, alpha = otherBallsAlpha)
        }
    }
}

@Composable
private fun animateAnglesAsState(viewModel: TimerViewModel): List<Float> {
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
