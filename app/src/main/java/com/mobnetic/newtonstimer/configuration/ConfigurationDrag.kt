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
package com.mobnetic.newtonstimer.configuration

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import com.mobnetic.newtonstimer.atanDegree
import com.mobnetic.newtonstimer.balls.BallSize

fun Modifier.configurationDragModifier(ballSize: BallSize, onAngleChanged: (Float) -> Unit, onDragEnd: () -> Unit) = composed {
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }
    pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { draggedOffset = Offset.Zero },
            onDragEnd = { onDragEnd() },
            onDrag = { change, dragOffsetDelta ->
                change.consumeAllChanges()
                draggedOffset += dragOffsetDelta
                val angle = atanDegree(-draggedOffset.x / (ballSize.stringLengthToBallCenter + draggedOffset.y))
                onAngleChanged(angle)
            }
        )
    }
}
