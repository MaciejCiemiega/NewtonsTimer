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
package com.mobnetic.newtonstimer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobnetic.newtonstimer.timer.NewtonsTimerScreen
import com.mobnetic.newtonstimer.timer.TimerViewModel
import com.mobnetic.newtonstimer.ui.setSystemBarsColor
import com.mobnetic.newtonstimer.ui.theme.Colors
import com.mobnetic.newtonstimer.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TimerViewModel = viewModel()
            MyTheme(darkMode = viewModel.darkMode) {
                val systemBarsColor by animateColorAsState(Colors.systemBars)
                setSystemBarsColor(systemBarsColor)

                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val backgroundColor by animateColorAsState(MaterialTheme.colors.background)
    Surface(color = backgroundColor) {
        NewtonsTimerScreen()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkMode = true) {
        MyApp()
    }
}
