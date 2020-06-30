package com.crp.dailynasa

import android.app.Application
import com.crp.dailynasa.di.networkModule
import com.crp.dailynasa.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NasaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@NasaApp)
            modules(listOf(networkModule, viewModelModule))
        }

    }
}