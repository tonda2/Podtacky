package cz.tonda2.podtacky.features.profile.di

import cz.tonda2.podtacky.features.profile.data.UserRepository
import cz.tonda2.podtacky.features.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val profileModule = module {
    singleOf(::UserRepository)
    viewModelOf(::ProfileViewModel)
}
