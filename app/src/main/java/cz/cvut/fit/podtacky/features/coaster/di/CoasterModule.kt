package cz.cvut.fit.podtacky.features.coaster.di

import cz.cvut.fit.podtacky.core.data.db.CoasterDatabase
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coasterModule = module {
    single { get<CoasterDatabase>().coasterDao() }
    factoryOf(::CoasterLocalDataSource)
    singleOf(::CoasterRepository)

    viewModelOf(::ListViewModel)
}