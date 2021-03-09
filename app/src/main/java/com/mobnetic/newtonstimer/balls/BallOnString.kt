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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.BallOnString(angle: Float, modifier: Modifier = Modifier) {
    val swingTransformation = Modifier.graphicsLayer(
        rotationZ = angle,
        transformOrigin = BALL_ON_STRING_TRANSFORMATION_ORIGIN
    )

    Column(
        modifier = modifier
            .weight(1f)
            .then(swingTransformation)
    ) {
        String(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
        )
        Ball()
    }
}

@Composable
private fun String(modifier: Modifier) {
    val stringColor by animateColorAsState(MaterialTheme.colors.onBackground.copy(alpha = 0.5f))
    Box(
        modifier = modifier
            .width(STRING_THICKNESS_DP)
            .background(stringColor)
    )
}

@Composable
private fun Ball() {
    val ballColor by animateColorAsState(MaterialTheme.colors.primary)
    val borderColor by animateColorAsState(MaterialTheme.colors.onBackground.copy(alpha = 0.04f))
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(ballColor, shape = CircleShape)
            .border(BORDER_THICKNESS_DP, borderColor, CircleShape)
    )
}

private val BALL_ON_STRING_TRANSFORMATION_ORIGIN = TransformOrigin(0.5f, 0f)
private val STRING_THICKNESS_DP = 1.dp
private val BORDER_THICKNESS_DP = 1.dp
