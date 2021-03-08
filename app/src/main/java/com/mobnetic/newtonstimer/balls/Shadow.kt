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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mobnetic.newtonstimer.sinDegree
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_ANGLE
import com.mobnetic.newtonstimer.ui.theme.Colors
import kotlin.math.abs

@Composable
fun Shadow(angle: Float, ballSize: BallSize) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.dp)
            .shadowTransformation(angle, ballSize)
    ) { ShadowGradientCircle() }
}

@Composable
private fun ShadowGradientCircle() {
    val shadowColor = Colors.ballShadow
    val gradient = remember(shadowColor) { createShadowGradient(shadowColor) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(gradient)
    )
}

private fun createShadowGradient(shadowColor: Color) = Brush.radialGradient(
    SHADOW_GRADIENT_SOLID_COLOR_STOP to shadowColor,
    1.0f to Color.Transparent,
)

private fun Modifier.shadowTransformation(angle: Float, ballSize: BallSize) = composed {
    val translationX = -sinDegree(angle) * ballSize.stringLengthToBallCenter
    val distanceFromGroundMultiplier = (1 - (abs(angle) / MAX_ANGLE) * (1 - SHADOW_MIN_SCALE)).coerceIn(SHADOW_MIN_SCALE, 1f)

    Modifier.graphicsLayer(
        scaleX = SHADOW_GRADIENT_UPSCALE * distanceFromGroundMultiplier,
        scaleY = SHADOW_GRADIENT_UPSCALE * distanceFromGroundMultiplier / SHADOW_ASPECT_RATIO,
        translationX = translationX,
        translationY = with(LocalDensity.current) { SHADOW_TOP_OFFSET.toPx() }
    )
}

private const val SHADOW_ASPECT_RATIO = 5f
private const val SHADOW_GRADIENT_UPSCALE = 1.25f
private const val SHADOW_GRADIENT_SOLID_COLOR_STOP = 0.7f
private val SHADOW_TOP_OFFSET = 32.dp
private const val SHADOW_MIN_SCALE = 0.15f
