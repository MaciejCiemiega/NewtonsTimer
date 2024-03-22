package com.mobnetic.newtonstimer.audio

import android.app.Application
import android.media.MediaPlayer
import dev.icerock.moko.resources.AssetResource

actual class AudioPlayer(private val application: Application) {

    private var player: MediaPlayer? = null

    actual fun prepare(resource: AssetResource) {
        release()

        player = MediaPlayer().apply {
            setDataSource(application.assets.openFd(resource.path))
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