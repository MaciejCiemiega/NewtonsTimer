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
package com.mobnetic.newtonstimer.unused.keypad

import com.mobnetic.newtonstimer.unused.keypad.Direction.LEFT
import com.mobnetic.newtonstimer.unused.keypad.Direction.RIGHT
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

interface KeypadAction {

    fun modifyDuration(durationMillis: Long): Long

    enum class Number(private val number: Int) : KeypadAction {
        Key1(1),
        Key2(2),
        Key3(3),
        Key4(4),
        Key5(5),
        Key6(6),
        Key7(7),
        Key8(8),
        Key9(9),
        Key0(0);

        override fun modifyDuration(durationMillis: Long): Long {
            val digits = asDigitArray(durationMillis)
            return when {
                digits.first() != 0 -> durationMillis
                else -> digits.pushZeroFrom(RIGHT).apply { set(lastIndex, number) }.asMillis()
            }
        }
    }

    object Backspace : KeypadAction {
        override fun modifyDuration(durationMillis: Long): Long {
            return asDigitArray(durationMillis).pushZeroFrom(LEFT).asMillis()
        }
    }
}

private enum class Direction(val indexDirection: Int) {
    LEFT(-1), RIGHT(1)
}

private fun asDigitArray(millis: Long): IntArray {
    val hours = MILLISECONDS.toHours(millis).toInt() % 100
    val minutes = MILLISECONDS.toMinutes(millis).toInt() % 60
    val seconds = MILLISECONDS.toSeconds(millis).toInt() % 60
    return intArrayOf(hours / 10, hours % 10, minutes / 10, minutes % 10, seconds / 10, seconds % 10)
}

private fun IntArray.pushZeroFrom(direction: Direction) = IntArray(size) { index ->
    getOrNull(index + direction.indexDirection) ?: 0
}

private fun IntArray.asMillis(): Long {
    val hours = (get(0) * 10 + get(1)).toLong()
    val minutes = (get(2) * 10 + get(3)).toLong()
    val seconds = (get(4) * 10 + get(5)).toLong()
    return HOURS.toMillis(hours) + MINUTES.toMillis(minutes) + SECONDS.toMillis(seconds)
}
