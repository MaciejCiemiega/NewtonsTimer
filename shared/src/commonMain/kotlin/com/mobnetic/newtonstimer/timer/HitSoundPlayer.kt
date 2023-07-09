package com.mobnetic.newtonstimer.timer

import com.mobnetic.newtonstimer.MR
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HitSoundPlayer : KoinComponent {

    private val mediaPlayer: AudioPlayer by inject()

    init {
        mediaPlayer.prepare(MR.assets.ball_hit)
    }

    fun playHitSound(volume: Float) {
        mediaPlayer.setVolume(volume)
        mediaPlayer.start()
    }

    fun release() {
        mediaPlayer.release()
    }
}
