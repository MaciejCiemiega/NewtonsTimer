package com.mobnetic.newtonstimer.audio

expect class AudioPlayer {
    fun prepare(audioFile: AudioFile)
    fun setVolume(volume: Float)
    fun start()
    fun release()
}