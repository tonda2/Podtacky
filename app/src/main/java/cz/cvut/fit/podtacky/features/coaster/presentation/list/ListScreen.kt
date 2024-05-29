package cz.cvut.fit.podtacky.features.coaster.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.cvut.fit.podtacky.R
import cz.cvut.fit.podtacky.core.presentation.Screen
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navController: NavController
) {
    val screenState by viewModel.screenStateLiveData.observeAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddScreen.route) },
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp, 64.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_coaster_button))
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.list_title))
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_button)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(screenState?.coasters ?: emptyList()) { coaster ->
                CoasterCard(
                    coaster = coaster
                ) {
                    navController.navigate(Screen.DetailScreen.route + "/${coaster.coasterId}")
                }
            }
        }
    }
}

@Composable
fun CoasterCard(
    coaster: Coaster,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        CoasterItem(coaster,
            Modifier
                .fillMaxWidth()
                .padding(8.dp))
    }
}

@Composable
private fun CoasterItem(coaster: Coaster, modifier: Modifier) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(size = 60.dp),
                model = coaster.frontUri,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = coaster.brewery,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = coaster.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
