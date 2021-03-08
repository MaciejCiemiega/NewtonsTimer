package com.mobnetic.newtonstimer.configuration

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.State
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

fun Modifier.configurationDragModifier(ballSize: State<BallSize>, onAngleChanged: (Float) -> Unit, onStarted: () -> Unit) = composed {
    var draggedOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { draggedOffset = Offset(0f, 0f) },
            onDragEnd = { onStarted() },
            onDrag = { change, dragOffsetDelta ->
                change.consumeAllChanges()
                draggedOffset += dragOffsetDelta
                val angle = atanDegree(-draggedOffset.x / (ballSize.value.stringLengthToBallCenter + draggedOffset.y))
                onAngleChanged(angle)
            })
    }
}