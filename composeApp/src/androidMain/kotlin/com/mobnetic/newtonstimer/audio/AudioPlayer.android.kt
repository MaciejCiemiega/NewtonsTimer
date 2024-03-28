package com.mobnetic.newtonstimer.audio

import android.app.Application
import android.media.MediaPlayer

actual class AudioPlayer(private val application: Application) {

    private var player: MediaPlayer? = null

    actual fun prepare(audioFile: AudioFile) {
        release()

        player = MediaPlayer().apply {
            setDataSource(application.assets.openFd(audioFile.fileNameWithExtension))
            prepare()
        }
    }

    actual fun setVolume(volume: Float) {
        player?.setVolume(volume, volume)
    }

    actual fun start() {
        player?.start()
    }

    actual fun release() {
        try {
            player?.stop()
            player?.release()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}