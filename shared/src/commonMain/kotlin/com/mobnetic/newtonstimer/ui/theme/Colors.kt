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

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal object Colors {
    val androidGreen = Color(0xFF3DDC84)
    val navy = Color(0xFF073042)
    val purplish = Color(0xFF880E4F)
    val lightBlue = Color(0xFFE0F7FA)

    private val ballShadowColorDark = Color.Black.copy(alpha = 0.3f)
    private val ballShadowColorLight = Color.Black.copy(alpha = 0.1f)
    val systemBarsScrimLight = Color(0x30FFFFFF)
    val systemBarsScrimDark = Color(0x30000000)

    val ballShadow @Composable get() = if (MaterialTheme.colors.isLight) ballShadowColorLight else ballShadowColorDark
}
