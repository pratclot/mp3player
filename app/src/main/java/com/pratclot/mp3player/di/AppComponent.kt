package com.pratclot.mp3player.di

import android.content.Context
import com.pratclot.mp3player.MainActivity
import com.pratclot.mp3player.fragments.FragmentExo
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun injectFragment(fragment: FragmentExo)
}
