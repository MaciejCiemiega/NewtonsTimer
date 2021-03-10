# Newton's Timer

![Workflow result](https://github.com/MaciejCiemiega/NewtonsTimer/workflows/Check/badge.svg)


## :scroll: Description
Simple timer app inspired by Newton's Cradle. Created in Jetpack Compose for [#AndroidDevChallenge](https://developer.android.com/dev-challenge).


## :bulb: Motivation and Context
The goal was to create something simple yet playful to demonstrate (and evaluate) capabilities of Jetpack Compose.  
The result is a (up to) 1 minute timer trying to recreate Newton's Cradle.

Some highlights and links to code:
- Drawing first ball trajectory and hint animation to setup the timer:
  - Usage of `drawArc` with `dashPathEffect` and animated `clipPath` - [code](/app/src/main/java/com/mobnetic/newtonstimer/configuration/ConfigurationHint.kt)
- Playful way of setting up timer by dragging first ball. Timer starts as soon as you lift your finger up:
  - Usage of `pointerInput` and `detectDragGestures` - [code](/app/src/main/java/com/mobnetic/newtonstimer/configuration/ConfigurationDrag.kt)
- Smooth transitioning between states:
  - Configuration mode and started mode - [code](app/src/main/java/com/mobnetic/newtonstimer/timer/NewtonsTimerScreen.kt)
- Swinging animation is controlling animation from ViewModel and can be paused/resumed:
  - `TargetBasedAnimation` + update loop in a Coroutine - [code](/app/src/main/java/com/mobnetic/newtonstimer/timer/TimerViewModel.kt)
- Trying to recreate gravitational swing with hit rebound and colliding balls:
  - Custom `Easing` and many parameters - [code](/app/src/main/java/com/mobnetic/newtonstimer/balls/SwingAnimation.kt)
- Shadows are blurred and scaled down if ball is swinging higher from the ground:
  - `Brush.radialGradient` and transformations using `graphicsLayer` - [code](/app/src/main/java/com/mobnetic/newtonstimer/balls/Shadow.kt)
- Playing hit sounds when balls collide, synchronized with animation.
- Swinging amplitude and hit sound volume decrease with time, reaching 0 at the end of the timer.
- Timer's display is styled with two font sizes and colors. Also font size adjusts to the available space:
  - Using `AnnotatedString.Builder` - [code](app/src/main/java/com/mobnetic/newtonstimer/timer/Display.kt)
- Animated change between dark and light mode.
- Support for both portrait and landscape orientations.


## :camera_flash: Screenshots
<img src="/results/video.gif" width="360">&emsp;<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
