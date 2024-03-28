package com.mobnetic.newtonstimer.audio

import org.w3c.dom.Audio

actual class AudioPlayer {

    private var audio: Audio? = null

    actual fun prepare(audioFile: AudioFile) {
        release()

        audio = Audio(audioFile.path)
    }

    actual fun setVolume(volume: Float) {
        audio?.volume = volume.toDouble()
    }

    actual fun start() {
        audio?.play()
    }

    actual fun release() {
        audio?.pause()
        audio = null
    }
}