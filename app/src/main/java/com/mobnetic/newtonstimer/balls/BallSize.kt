package com.mobnetic.newtonstimer.balls

data class BallSize(
    val ballRadius: Int = 0,
    val ballWithString: Int = 0
) {
    val stringLengthToBallCenter = (ballWithString - ballRadius).toFloat()
}