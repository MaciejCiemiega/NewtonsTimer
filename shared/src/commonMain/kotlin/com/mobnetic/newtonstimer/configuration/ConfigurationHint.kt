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

package com.mobnetic.newtonstimer.configuration

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import com.mobnetic.newtonstimer.balls.BallSize
import com.mobnetic.newtonstimer.timer.TimerViewModel

@Composable
fun ConfigurationHint(configuredAngle: Float, ballSize: BallSize) {
    val color by animateColorAsState(MaterialTheme.colors.onBackground)
    val animateStartAngle by animatedHintArcStartAngle()
    Box(
        Modifier.drawBehind {
            drawHintArc(ballSize, sweepAngle = TimerViewModel.MAX_ANGLE, color, alpha = 0.3f)

            if (configuredAngle > 0f) {
                drawHintArc(ballSize, sweepAngle = configuredAngle, color)
            } else {
                drawAnimatedHintArc(ballSize, animatedHintStartAngle = animateStartAngle, color)
            }
        }
    )
}

private fun DrawScope.drawHintArc(ballSize: BallSize, sweepAngle: Float, color: Color, alpha: Float = 1f) {
    val dashInterval = ARC_STROKE_DASH_INTERVAL.toPx()
    drawArc(
        color = color,
        startAngle = 90f,
        sweepAngle = sweepAngle,
        topLeft = ballSize.arcOffset(),
        size = ballSize.arcSize(),
        alpha = alpha,
        useCenter = false,
        style = Stroke(
            ARC_STROKE_WIDTH.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashInterval, dashInterval))
        )
    )
}

private fun DrawScope.drawAnimatedHintArc(ballSize: BallSize, animatedHintStartAngle: Float, color: Color) {
    clipPath(animatedHintClipPath(ballSize, animatedStartAngle = animatedHintStartAngle)) {
        drawHintArc(ballSize, sweepAngle = TimerViewModel.MAX_ANGLE, color)
    }
}

@Composable
private fun animatedHintArcStartAngle(): State<Float> {
    val transition = rememberInfiniteTransition()
    return transition.animateFloat(
        initialValue = 90f - ARC_ANIMATED_HINT_SWEEP_ANGLE_DEGREES,
        targetValue = 90f + TimerViewModel.MAX_ANGLE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ARC_ANIMATED_HINT_ANIM_DURATION,
                delayMillis = ARC_ANIMATED_HINT_ANIM_DELAY,
            ),
            repeatMode = RepeatMode.Restart
        )
    )
}

private fun DrawScope.animatedHintClipPath(ballSize: BallSize, animatedStartAngle: Float) = Path().apply {
    moveTo(0f, 0f)
    arcTo(
        rect = ballSize.arcRect(additionalRadius = ARC_STROKE_WIDTH.toPx()),
        startAngleDegrees = animatedStartAngle,
        sweepAngleDegrees = ARC_ANIMATED_HINT_SWEEP_ANGLE_DEGREES,
        forceMoveTo = false
    )
}

private fun BallSize.arcOffset(additionalRadius: Float = 0f): Offset {
    val radius = stringLengthToBallCenter + additionalRadius
    return Offset(x = -radius + ballRadius, y = -radius)
}

private fun BallSize.arcSize(additionalRadius: Float = 0f): Size {
    val radius = stringLengthToBallCenter + additionalRadius
    return Size(width = radius, height = radius).times(2f)
}

private fun BallSize.arcRect(additionalRadius: Float = 0f) = Rect(arcOffset(additionalRadius), arcSize(additionalRadius))

private val ARC_STROKE_WIDTH = 2.dp
private val ARC_STROKE_DASH_INTERVAL = ARC_STROKE_WIDTH * 4
private const val ARC_ANIMATED_HINT_SWEEP_ANGLE_DEGREES = 5f
private const val ARC_ANIMATED_HINT_ANIM_DURATION = 1200
private const val ARC_ANIMATED_HINT_ANIM_DELAY = 600
