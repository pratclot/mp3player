package com.pratclot.mp3player

import android.app.Application
import com.pratclot.mp3player.di.AppComponent
import com.pratclot.mp3player.di.DaggerAppComponent

open class Mp3Player : Application() {
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}
