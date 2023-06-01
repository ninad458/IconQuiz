package com.enigma.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope

@HiltAndroidApp
class MyApplication : Application() {

    val applicationScope = MainScope()

}