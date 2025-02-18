package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.BottomBar
import cz.tonda2.podtacky.core.presentation.BottomBarScreenIndex
import cz.tonda2.podtacky.core.presentation.CoasterList
import cz.tonda2.podtacky.core.presentation.ExpandableFAB
import cz.tonda2.podtacky.core.presentation.FABItem
import cz.tonda2.podtacky.core.presentation.Grayable
import cz.tonda2.podtacky.core.presentation.ListSortBottomSheet
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navController: NavController
) {
    val listUiState by viewModel.listUiState.collectAsStateWithLifecycle()
    var showSortBottomSheet by remember { mutableStateOf(false) }
    var isFabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExpandableFAB(
                expanded = isFabExpanded,
                onClick = { isFabExpanded = !isFabExpanded },
                options = listOf(
                    FABItem(
                        ImageVector.vectorResource(id = R.drawable.baseline_create_new_folder_24),
                        title = stringResource(R.string.add_folder),
                        onClick = { navController.navigate(Screen.FolderScreen.route + "/-?${Screen.FolderScreen.SHOW_ADD_POPUP}=true") }
                    ),
                    FABItem(
                        Icons.Filled.Add,
                        title = stringResource(R.string.add_coaster),
                        onClick = { navController.navigate(Screen.EditScreen.route + "/-1") }
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
                    Column {
                        Text(text = stringResource(R.string.list_title))
                        Text(
                            text =
                            when (listUiState.coasters.size) {
                                1 -> {
                                    stringResource(R.string._1_podtacek)
                                }

                                in 1..4 -> {
                                    stringResource(
                                        R.string._2_4_podtacky,
                                        listUiState.coasters.size
                                    )
                                }

                                else -> stringResource(
                                    R.string._5_podtacku,
                                    listUiState.coasters.size
                                )
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
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
            BottomBar(navController = navController, selectedInd = BottomBarScreenIndex.LIST.index)
        }
    ) { paddingValues ->
        val coasters = listUiState.coasters

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

            CoasterList(
                coasters = coasters,
                emptyText = stringResource(R.string.no_coasters),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onItemClick = { coaster ->
                    navController.navigate(Screen.DetailScreen.route + "/${coaster.coasterId}")
                }
            )
        }
    }
}