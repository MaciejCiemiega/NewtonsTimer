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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobnetic.newtonstimer.balls.SHADOW_TOP_OFFSET
import com.mobnetic.newtonstimer.balls.SwingingBalls
import com.mobnetic.newtonstimer.sinDegree
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_ANGLE
import com.mobnetic.newtonstimer.ui.isLandscape

@Preview(widthDp = 400, heightDp = 700)
@Composable
fun NewtonsTimerScreen() {
    val timerViewModel: TimerViewModel = viewModel()

    BoxWithConstraints {
        if (isLandscape) {
            NewtonsTimerLandscape(timerViewModel)
        } else {
            NewtonsTimerPortrait(timerViewModel)
        }
    }
}

@Composable
private fun NewtonsTimerPortrait(viewModel: TimerViewModel) {
    Column {
        Column(
            modifier = Modifier
                .animateContentSize()
                .weight(0.88f)
        ) {
            val isConfigured = viewModel.state is TimerState.Configured
            val ballsInnerRatio = BALLS_INNER_ASPECT_RATIO_PORTRAIT
            val ballsOuterRatio by animateFloatAsState(if (isConfigured) 1.1f * sinDegree(MAX_ANGLE) + ballsInnerRatio else sinDegree(MAX_ANGLE) + (ballsInnerRatio / 2f))
            BoxWithConstraints(
                modifier = Modifier.aspectRatio(ballsOuterRatio),
                contentAlignment = Alignment.Center
            ) {
                val translationX by animateFloatAsState(targetValue = if (isConfigured) 0f else constraints.maxWidth / 2f)
                SwingingBalls(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(ballsInnerRatio)
                        .graphicsLayer(translationX = translationX)
                )
            }
            Display(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        ButtonsBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .weight(0.1f)
        )
    }
}

@Composable
private fun NewtonsTimerLandscape(viewModel: TimerViewModel) {
    Row {
        Column(
            modifier = Modifier
                .animateContentSize()
                .weight(1.5f)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Display(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            ButtonsBar(modifier = Modifier.fillMaxWidth())
        }

        val isConfigured = viewModel.state is TimerState.Configured
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = SHADOW_TOP_OFFSET + 24.dp),
            contentAlignment = Alignment.Center
        ) {
            val translationX by animateFloatAsState(targetValue = if (isConfigured) 0f else constraints.maxWidth / 2f)
            SwingingBalls(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(BALLS_INNER_ASPECT_RATIO_LANDSCAPE)
                    .graphicsLayer(translationX = translationX)
            )
        }
    }
}

private const val BALLS_INNER_ASPECT_RATIO_PORTRAIT = 0.5f
private const val BALLS_INNER_ASPECT_RATIO_LANDSCAPE = 0.55f
