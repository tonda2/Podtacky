package cz.tonda2.podtacky.features.folder.di

import cz.tonda2.podtacky.core.data.db.CoasterDatabase
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.presentation.list.FolderListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val folderModule = module {
    single { get<CoasterDatabase>().folderDao() }
    singleOf(::FolderRepository)

    viewModelOf(::FolderListViewModel)
}