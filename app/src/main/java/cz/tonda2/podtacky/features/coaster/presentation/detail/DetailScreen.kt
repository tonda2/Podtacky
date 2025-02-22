package cz.tonda2.podtacky.features.coaster.presentation.detail

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.DeleteConfirmation
import cz.tonda2.podtacky.core.presentation.PageIndicator
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.presentation.LoadingScreen
import cz.tonda2.podtacky.features.coaster.presentation.ScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    screenState.coaster?.let { coaster ->
        Scaffold(
            topBar = {
                DetailTopBar(
                    coaster = coaster,
                    onBackClick = { navController.navigateUp() },
                    onDeleteClick = { showDialog = true }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.EditScreen.route + "/${coaster.coasterId}?${Screen.EditScreen.FOLDER_UID}=${coaster.folderUid ?: "-"}"
                        )
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(64.dp, 64.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Edit, stringResource(R.string.edit_button))
                }
            }
        ) { paddingValues ->
            if (showDialog) {
                DeleteConfirmation(
                    onConfirm = {
                        viewModel.delete(context)
                        navController.navigate(Screen.ListScreen.route)
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            } else {
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (screenState.state) {
                        ScreenState.Fill -> CoasterDetail(coaster, screenState.folderName) { index ->
                            navController.navigate(
                                Screen.LargePhotoScreen.route + "/${coaster.coasterId}?${Screen.LargePhotoScreen.START_INDEX}=${index}"
                            )
                        }
                        ScreenState.Loading -> LoadingScreen(
                            modifier = Modifier.padding(
                                paddingValues
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    coaster: Coaster,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        title = {
            Text(text = coaster.brewery)
        },
        actions = {
            IconButton(onClick = {
                onDeleteClick()
            }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_button)
                )
            }
        }
    )
}

@Composable
fun CoasterDetail(
    coaster: Coaster,
    folderName: String,
    onPhotoClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            DetailTopPart(coaster = coaster, onPhotoClick = onPhotoClick)
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(14.dp))
            DetailLowerPart(coaster, folderName)
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailTopPart(
    coaster: Coaster,
    onPhotoClick: (Int) -> Unit
) {
    Row(modifier = Modifier.padding(all = 16.dp)) {
        Box(
            modifier = Modifier.size(164.dp),
            contentAlignment = Alignment.Center
        ) {
            val pagerState = rememberPagerState(pageCount = { 2 })

            HorizontalPager(
                state = pagerState
            ) { page ->
                val uri = when (page) {
                    0 -> coaster.frontUri
                    1 -> coaster.backUri
                    else -> Uri.EMPTY
                }
                AsyncImage(
                    modifier = Modifier
                        .size(size = 164.dp)
                        .clickable { onPhotoClick(page) },
                    model = uri,
                    contentDescription = stringResource(R.string.coaster_image_cd, page)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            PageIndicator(count = pagerState.pageCount, current = pagerState.currentPage)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Field(
                title = stringResource(R.string.brewery),
                value = coaster.brewery,
                styleTitle = MaterialTheme.typography.bodyMedium,
                colorTitle = MaterialTheme.colorScheme.onSecondary,
                styleValue = MaterialTheme.typography.headlineMedium,
                colorValue = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Field(
                title = stringResource(R.string.city),
                value = coaster.city,
                styleTitle = MaterialTheme.typography.bodyMedium,
                colorTitle = MaterialTheme.colorScheme.onSecondary,
                styleValue = MaterialTheme.typography.headlineMedium,
                colorValue = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun DetailLowerPart(
    coaster: Coaster,
    folderName: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Field(title = stringResource(R.string.description), value = coaster.description)
        Field(title = stringResource(R.string.count), value = coaster.count.toString())
        Field(title = stringResource(R.string.added_date), value = coaster.dateAdded)
        Field(title = stringResource(R.string.slozka), value = folderName)

        if (coaster.uploaded) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_cloud_done_24),
                contentDescription = stringResource(R.string.backed_up),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun Field(
    title: String,
    value: String,
    styleTitle: TextStyle = MaterialTheme.typography.bodySmall,
    colorTitle: Color = MaterialTheme.colorScheme.onSecondary,
    styleValue: TextStyle = MaterialTheme.typography.headlineSmall,
    colorValue: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Column(
        modifier = Modifier
            .semantics {
                contentDescription = "$title is $value"
            }
    ) {
        Text(
            text = title,
            style = styleTitle,
            color = colorTitle,
        )
        Text(
            text = value,
            style = styleValue,
            color = colorValue,
        )
    }
}