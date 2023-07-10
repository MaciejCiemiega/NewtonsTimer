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
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobnetic.newtonstimer.balls.SHADOW_TOP_OFFSET
import com.mobnetic.newtonstimer.balls.SwingingBallsContainer
import com.mobnetic.newtonstimer.sinDegree
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_ANGLE
import com.mobnetic.newtonstimer.ui.isLandscape

//@Preview(widthDp = 400, heightDp = 700)
@Composable
fun NewtonsTimerScreen(timerViewModel: TimerViewModel) {
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
            val ballsOuterRatio by animateFloatAsState(
                targetValue = when (viewModel.isConfigured) {
                    true -> 1.1f * sinDegree(MAX_ANGLE) + BALLS_INNER_ASPECT_RATIO_PORTRAIT
                    else -> sinDegree(MAX_ANGLE) + (BALLS_INNER_ASPECT_RATIO_PORTRAIT / 2f)
                },
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
            SwingingBallsContainer(
                viewModel = viewModel,
                ballsInnerRatio = BALLS_INNER_ASPECT_RATIO_PORTRAIT,
                modifier = Modifier.aspectRatio(ballsOuterRatio)
            )
            Display(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        ButtonsBar(
            viewModel = viewModel,
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
                .weight(1.2f)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.25f))
            Display(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            ButtonsBar(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.3f))
        }

        SwingingBallsContainer(
            viewModel = viewModel,
            ballsInnerRatio = BALLS_INNER_ASPECT_RATIO_LANDSCAPE,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = SHADOW_TOP_OFFSET + 24.dp)
        )
    }
}

private const val BALLS_INNER_ASPECT_RATIO_PORTRAIT = 0.5f
private const val BALLS_INNER_ASPECT_RATIO_LANDSCAPE = 0.55f
