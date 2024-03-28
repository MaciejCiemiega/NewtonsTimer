package com.mobnetic.newtonstimer.audio

data class AudioFile(
    val fileName: String,
    val extension: String,
) {
    val fileNameWithExtension = "$fileName.$extension"
    val composeResourcedDirName = "compose-resources"
    val filesDirName = "files"
    val path = "$filesDirName/$fileNameWithExtension"
}