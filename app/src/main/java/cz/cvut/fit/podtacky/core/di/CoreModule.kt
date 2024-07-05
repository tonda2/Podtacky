package cz.cvut.fit.podtacky.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import cz.cvut.fit.podtacky.core.data.db.CoasterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single { CoasterDatabase.newInstance(androidContext()) }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
}