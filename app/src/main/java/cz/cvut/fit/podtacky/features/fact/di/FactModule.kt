package cz.cvut.fit.podtacky.features.fact.di

import org.koin.core.module.dsl.factoryOf
import cz.cvut.fit.podtacky.features.fact.data.api.FactRemoteDataSource
import cz.cvut.fit.podtacky.features.fact.data.FactRepository
import cz.cvut.fit.podtacky.features.fact.data.api.FactApiDescription
import cz.cvut.fit.podtacky.features.fact.presentation.FactViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

val factModule = module {
    single { get<Retrofit>().create(FactApiDescription::class.java) }
    factoryOf(::FactRemoteDataSource)
    singleOf(::FactRepository)
    viewModelOf(::FactViewModel)
}