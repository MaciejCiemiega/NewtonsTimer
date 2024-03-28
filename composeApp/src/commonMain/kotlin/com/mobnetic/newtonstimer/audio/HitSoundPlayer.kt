package com.mobnetic.newtonstimer.audio

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HitSoundPlayer : KoinComponent {

    private val mediaPlayer: AudioPlayer by inject()

    init {
        mediaPlayer.prepare(BallHitAudioFile)
    }

    fun playHitSound(volume: Float) {
        mediaPlayer.setVolume(volume)
        mediaPlayer.start()
    }

    fun release() {
        mediaPlayer.release()
    }
}

private val BallHitAudioFile = AudioFile(fileName = "ball_hit", extension = "mp3")