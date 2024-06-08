package cz.cvut.fit.podtacky.features.coaster.di

import cz.cvut.fit.podtacky.core.data.db.CoasterDatabase
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.presentation.add.AddViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.detail.DetailViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.edit.EditViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.search.SearchViewModel
import cz.cvut.fit.podtacky.features.coaster.presentation.large_photo.LargePhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coasterModule = module {
    single { get<CoasterDatabase>().coasterDao() }
    factoryOf(::CoasterLocalDataSource)
    singleOf(::CoasterRepository)

    viewModelOf(::ListViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AddViewModel)
    viewModelOf(::EditViewModel)
    viewModelOf(::LargePhotoViewModel)
}