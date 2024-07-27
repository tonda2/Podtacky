package cz.tonda2.podtacky.features.coaster.presentation.edit

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.data.compressImage
import cz.tonda2.podtacky.core.data.createImageFile
import cz.tonda2.podtacky.core.presentation.PageIndicator
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.presentation.LoadingScreen
import cz.tonda2.podtacky.features.coaster.presentation.ScreenState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditScreen(
    navController: NavController,
    viewModel: EditViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val permission = Manifest.permission.CAMERA
    val permissionState = rememberMultiplePermissionsState(listOf(permission))

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.deletePicture(screenState.frontUri, context)
                        viewModel.deletePicture(screenState.backUri, context)
                        navController.navigateUp()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                title = {
                    Text(text = screenState.title)
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
        if (permissionState.allPermissionsGranted) {
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
        else {
            LaunchedEffect(Unit) {
                permissionState.launchMultiplePermissionRequest()
            }
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
    val context = LocalContext.current

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
            PhotoSlider(
                photos = listOf(screenState.frontUri, screenState.backUri),
                modifier = Modifier.size(164.dp),
                onFrontUpdate = { uri -> viewModel.updateFrontUri(uri) },
                onBackUpdate = { uri -> viewModel.updateBackUri(uri) },
                onPhotoClick = { uri -> viewModel.deletePicture(uri, context) }
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoSlider(
    photos: List<Uri>,
    modifier: Modifier = Modifier,
    onFrontUpdate: (Uri) -> Unit,
    onBackUpdate: (Uri) -> Unit,
    onPhotoClick: (Uri) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val pagerState = rememberPagerState(pageCount = { photos.size })

        HorizontalPager(
            state = pagerState
        ) { page ->
            PictureBox(photos[page], page, onFrontUpdate, onBackUpdate, onPhotoClick)
        }

        PageIndicator(count = pagerState.pageCount, current = pagerState.currentPage)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PictureBox(
    uri: Uri,
    page: Int,
    onFrontUpdate: (Uri) -> Unit,
    onBackUpdate: (Uri) -> Unit,
    onPhotoClick: (Uri) -> Unit
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val permission = Manifest.permission.CAMERA
    val permissionState = rememberMultiplePermissionsState(listOf(permission))

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = compressImage(context, photoUri, compressRate = 0.2)

            when (page) {
                0 -> onFrontUpdate(photoUri)
                1 -> onBackUpdate(photoUri)
            }
        }
    }

    if (uri != Uri.EMPTY) {
        Box(
            modifier = Modifier.size(164.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(size = 164.dp)
                    .clickable { onPhotoClick(uri) },
                model = uri,
                contentDescription = stringResource(R.string.coaster_image_edit_screen)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(164.dp)
                .background(Color.Gray)
                .clickable {
                    if (permissionState.allPermissionsGranted) {
                        photoUri = createImageFile(context)
                        takePictureLauncher.launch(photoUri)
                    } else {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                contentDescription = stringResource(R.string.camera_icon_button),
                tint = Color.White
            )
        }
    }
}

@Composable
fun EntryField(
    query: String,
    placeholder: String,
    singleline: Boolean = true,
    onQueryChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        value = query,
        singleLine = singleline,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        },
        keyboardOptions = keyboardOptions
    )
}