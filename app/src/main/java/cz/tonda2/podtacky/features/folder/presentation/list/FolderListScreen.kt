package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.BottomBar
import cz.tonda2.podtacky.core.presentation.BottomBarScreenIndex
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderListScreen(
    viewModel: FolderListViewModel = koinViewModel(),
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.slozky))
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController, selectedInd = BottomBarScreenIndex.FOLDER.index)
        }
    ) { paddingValues ->
        Text("ahoj", modifier = Modifier.padding(paddingValues))
    }
}