package cz.cvut.fit.podtacky.core.di

import cz.cvut.fit.podtacky.core.data.api.RetrofitProvider
import cz.cvut.fit.podtacky.core.data.db.CoasterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single { CoasterDatabase.newInstance(androidContext()) }
    single { RetrofitProvider.provide() }
}