package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.BottomBar
import cz.tonda2.podtacky.core.presentation.BottomBarScreenIndex
import cz.tonda2.podtacky.core.presentation.DeleteConfirmation
import cz.tonda2.podtacky.core.presentation.ExpandableFAB
import cz.tonda2.podtacky.core.presentation.FABItem
import cz.tonda2.podtacky.core.presentation.FolderAndCoasterList
import cz.tonda2.podtacky.core.presentation.Grayable
import cz.tonda2.podtacky.core.presentation.ListSortBottomSheet
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderListScreen(
    viewModel: FolderListViewModel = koinViewModel(),
    navController: NavController,
    defaultShowAddPopup: Boolean
) {

    val screenState by viewModel.folderListUiState.collectAsStateWithLifecycle()

    var isFabExpanded by remember { mutableStateOf(false) }
    var showRenamePopup by remember { mutableStateOf(false) }
    var showDeletePopup by remember { mutableStateOf(false) }
    var showAddPopup by remember { mutableStateOf(defaultShowAddPopup) }
    var showSortBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExpandableFAB(
                expanded = isFabExpanded,
                onClick = { isFabExpanded = !isFabExpanded },
                options = listOf(
                    FABItem(
                        ImageVector.vectorResource(id = R.drawable.baseline_create_new_folder_24),
                        title = stringResource(R.string.add_folder),
                        onClick = {
                            isFabExpanded = false
                            showAddPopup = true
                        }
                    ),
                    FABItem(
                        Icons.Filled.Add,
                        title = stringResource(R.string.add_coaster),
                        onClick = { navController.navigate(Screen.EditScreen.route + "/-1?${Screen.EditScreen.FOLDER_UID}=${screenState.parentFolder?.folderUid ?: ""}") }
                    )
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp, 64.dp),
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_coaster_button))
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = screenState.parentFolder?.name ?: stringResource(R.string.slozky),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (screenState.parentFolder != null) {
                        IconButton(onClick = {
                            navController.navigate(Screen.FolderScreen.route + "/${screenState.parentFolder?.parentUid ?: "-"}?${Screen.FolderScreen.SHOW_ADD_POPUP}=false")
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_button)
                            )
                        }
                        IconButton(onClick = {
                            isFabExpanded = false
                            showSortBottomSheet = !showSortBottomSheet
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_sort_24),
                                contentDescription = stringResource(R.string.sort)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                selectedInd = BottomBarScreenIndex.FOLDER.index
            )
        }
    ) { paddingValues ->
        if (showAddPopup) {
            AddFolderPopup(
                onConfirm = {
                    viewModel.addFolder()
                    showAddPopup = false
                },
                onDismiss = {
                    showAddPopup = false
                    viewModel.updateNewFolderName("")
                },
                textValue = screenState.newFolderName,
                onTextValueChange = viewModel::updateNewFolderName,
                textFieldPlaceholder = stringResource(R.string.jmeno_slozky)
            )
        }
        else if (showRenamePopup) {
            AddFolderPopup(
                title = stringResource(R.string.prejmenovat_slozku),
                confirmButtonText = stringResource(R.string.prejmenovat),
                onConfirm = {
                    viewModel.renameFolder()
                    showRenamePopup = false
                },
                onDismiss = {
                    showRenamePopup = false
                },
                textValue = screenState.changedName,
                onTextValueChange = viewModel::updateRename,
                textFieldPlaceholder = stringResource(R.string.jmeno_slozky)
            )
        }
        else if (showDeletePopup) {
            DeleteConfirmation(
                text = stringResource(R.string.folder_delete_confirm),
                onConfirm = {
                    viewModel.deleteFolder()
                    showDeletePopup = false
                },
                onDismiss = {
                    showDeletePopup = false
                }
            )
        }
        else {
            Grayable(hidden = isFabExpanded, onClick = { isFabExpanded = false }) {
                if (showSortBottomSheet) {
                    ListSortBottomSheet(
                        options = CoasterSortType.entries,
                        selected = viewModel.getSelectedIndex(),
                        onDismissRequest = {
                            showSortBottomSheet = false
                        },
                        onOptionClick = { order ->
                            if (viewModel.updateSortOrder(order)) {
                                showSortBottomSheet = false
                            }
                        }
                    )
                }

                FolderAndCoasterList(
                    folders = screenState.subFolders,
                    coasters = screenState.coasters,
                    emptyText = stringResource(R.string.no_folders),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onFolderClick = { folder ->
                        navController.navigate(Screen.FolderScreen.route + "/${folder.folderUid}?${Screen.FolderScreen.SHOW_ADD_POPUP}=false")
                    },
                    onFolderDeleteClick = { folder ->
                        viewModel.setToDelete(folder)
                        showDeletePopup = true
                    },
                    onFolderRenameClick = { folder ->
                        viewModel.startRename(folder)
                        showRenamePopup = true
                    },
                    onCoasterClick = { coaster ->
                        navController.navigate(Screen.DetailScreen.route + "/${coaster.coasterId}")
                    }
                )
            }
        }
    }
}