package cz.cvut.fit.podtacky

import android.app.Application
import cz.cvut.fit.podtacky.core.di.coreModule
import cz.cvut.fit.podtacky.features.coaster.di.coasterModule
import cz.cvut.fit.podtacky.features.fact.di.factModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, coasterModule, factModule)
        }
    }
}