package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.BottomBar
import cz.tonda2.podtacky.core.presentation.BottomBarScreenIndex
import cz.tonda2.podtacky.core.presentation.CoasterList
import cz.tonda2.podtacky.core.presentation.ExpandableFAB
import cz.tonda2.podtacky.core.presentation.FABItem
import cz.tonda2.podtacky.core.presentation.Grayable
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navController: NavController
) {
    val screenState by viewModel.screenStateLiveData.observeAsState()
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
                        onClick = {}
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
                            when (screenState?.coasters?.size ?: 0) {
                                1 -> {
                                    stringResource(R.string._1_podtacek)
                                }

                                in 1..4 -> {
                                    stringResource(
                                        R.string._2_4_podtacky,
                                        screenState?.coasters?.size ?: 0
                                    )
                                }

                                else -> stringResource(
                                    R.string._5_podtacku,
                                    screenState?.coasters?.size ?: 0
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
        val coasters = screenState?.coasters ?: emptyList()

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSortBottomSheet(
    onDismissRequest: () -> Unit,
    selected: Int,
    options: List<CoasterSortType>,
    onOptionClick: (CoasterSortType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        ListSortBottomSheetContent(
            options = options,
            selected,
            onOptionClick = { order ->
                onOptionClick(order)
            }
        )
    }
}

@Composable
fun ListSortBottomSheetContent(
    options: List<CoasterSortType>,
    selected: Int,
    onOptionClick: (CoasterSortType) -> Unit
) {
    val selectedIndexState by remember { mutableIntStateOf(selected) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sort),
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp
        )

        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        onOptionClick(option)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (index == selectedIndexState) MaterialTheme.colorScheme.secondary else Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                if (index == selectedIndexState) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.selected),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}