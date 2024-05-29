package cz.cvut.fit.podtacky.features.coaster.presentation.edit

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.cvut.fit.podtacky.R
import cz.cvut.fit.podtacky.core.presentation.Screen
import cz.cvut.fit.podtacky.features.coaster.presentation.LoadingScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.ScreenState
import cz.cvut.fit.podtacky.features.coaster.presentation.add.EntryField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navController: NavController,
    viewModel: EditViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.edit_coaster_title))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveEdit()
                    navController.navigate(Screen.ListScreen.route)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp, 64.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    stringResource(R.string.save_changes_button)
                )
            }
        },
    ) { paddingValues ->
        when (screenState.state) {
            ScreenState.Fill -> EntryEditScreen(
                paddingValues = paddingValues,
                scrollState = scrollState,
                screenState = screenState,
                viewModel = viewModel
            )

            ScreenState.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun EntryEditScreen(
    paddingValues: PaddingValues,
    scrollState: ScrollState,
    screenState: EditScreenState,
    viewModel: EditViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(164.dp)
                    .background(Color.Gray)
                    .clickable {
                        // TODO vyfotit podtacek
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = stringResource(R.string.camera_icon_button),
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                EntryField(
                    query = screenState.brewery,
                    placeholder = stringResource(R.string.brewery),
                    onQueryChange = viewModel::updateBrewery,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                Spacer(modifier = Modifier.height(8.dp))
                EntryField(
                    query = screenState.city,
                    placeholder = stringResource(R.string.city),
                    onQueryChange = viewModel::updateCity,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        EntryField(
            query = screenState.description,
            placeholder = stringResource(R.string.description),
            onQueryChange = viewModel::updateDescription,
            singleline = false,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EntryField(
            query = screenState.date,
            placeholder = stringResource(R.string.added_date),
            onQueryChange = viewModel::updateDate
        )
        Spacer(modifier = Modifier.height(16.dp))
        EntryField(
            query = screenState.count,
            placeholder = stringResource(R.string.count),
            onQueryChange = viewModel::updateCount,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}