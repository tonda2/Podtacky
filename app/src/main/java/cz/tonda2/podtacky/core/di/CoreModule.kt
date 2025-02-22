package cz.tonda2.podtacky.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import cz.tonda2.podtacky.core.data.BackupManager
import cz.tonda2.podtacky.core.data.ImportManager
import cz.tonda2.podtacky.core.data.PreferencesManager
import cz.tonda2.podtacky.core.data.db.CoasterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    single { CoasterDatabase.newInstance(androidContext()) }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { PreferencesManager(androidContext()) }
    singleOf(::BackupManager)
    singleOf(::ImportManager)
}