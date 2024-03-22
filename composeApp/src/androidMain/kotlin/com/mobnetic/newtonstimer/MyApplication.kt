package com.mobnetic.newtonstimer

import android.app.Application

class MyApplication : Application() {
    init {
        startDi(this)
    }
}