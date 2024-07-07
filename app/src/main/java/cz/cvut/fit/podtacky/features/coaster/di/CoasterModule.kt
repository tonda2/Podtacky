package cz.cvut.fit.podtacky.features.coaster.di

import cz.cvut.fit.podtacky.core.data.db.CoasterDatabase
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.data.firebase.firestore.FirestoreRepository
import cz.cvut.fit.podtacky.features.coaster.data.firebase.storage.FirebaseStorageRepository
import cz.cvut.fit.podtacky.features.coaster.presentation.detail.DetailViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.edit.EditViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.large_photo.LargePhotoViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coasterModule = module {
    single { get<CoasterDatabase>().coasterDao() }
    factoryOf(::CoasterLocalDataSource)
    singleOf(::CoasterRepository)
    singleOf(::FirestoreRepository)
    singleOf(::FirebaseStorageRepository)

    viewModelOf(::ListViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::EditViewModel)
    viewModelOf(::LargePhotoViewModel)
}