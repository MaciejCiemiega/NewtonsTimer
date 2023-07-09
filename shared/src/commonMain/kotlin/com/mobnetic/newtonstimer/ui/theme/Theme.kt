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
package com.mobnetic.newtonstimer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Colors.androidGreen,
    primaryVariant = Colors.androidGreen,
    secondary = Colors.androidGreen,
    background = Colors.navy
)

private val LightColorPalette = lightColors(
    primary = Colors.purplish,
    primaryVariant = Colors.purplish,
    secondary = Colors.purplish,
    background = Colors.lightBlue
)

@Composable
fun MyTheme(darkMode: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkMode) DarkColorPalette else LightColorPalette,
        content = content
    )
}
