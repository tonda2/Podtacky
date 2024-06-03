package cz.cvut.fit.podtacky.features.fact.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.cvut.fit.podtacky.R
import cz.cvut.fit.podtacky.features.coaster.presentation.list.BottomBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactScreen(
    navController: NavController,
    viewModel: FactViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val BEER_IMAGE_URL = "https://madprg.com/wp-content/uploads/2023/01/Beers-in-Prague.jpg"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.fakta))
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController, isList = false)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = BEER_IMAGE_URL,
                contentDescription = stringResource(R.string.yummy),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.getFact()
                }
            ) {
                Text(text = stringResource(R.string.vygenerovat_fakt))
            }
            Text(text = screenState.fact)

        }
    }
}