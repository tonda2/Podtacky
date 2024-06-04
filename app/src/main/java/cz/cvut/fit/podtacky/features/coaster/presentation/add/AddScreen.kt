package cz.cvut.fit.podtacky.features.coaster.presentation.add

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import androidx.compose.material.icons.filled.Check
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
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cz.cvut.fit.podtacky.R
import cz.cvut.fit.podtacky.features.coaster.presentation.LoadingScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.ScreenState
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: AddViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.deletePictures(context)
                        navController.navigateUp()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.add_coaster))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.save()
                    navController.navigateUp()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp, 64.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Check, stringResource(R.string.add_coaster_button))
            }
        },
    ) { paddingValues ->
        when (screenState.state) {
            ScreenState.Fill -> EntryScreen(
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
fun EntryScreen(
    paddingValues: PaddingValues,
    scrollState: ScrollState,
    screenState: AddScreenState,
    viewModel: AddViewModel
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
            PhotoSlider(
                photos = listOf(screenState.frontUri, screenState.backUri),
                viewModel = viewModel,
                modifier = Modifier.size(164.dp)
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
    viewModel: AddViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = rememberPagerState(
                pageCount = { photos.size }
            )
        ) { page ->
            PictureBox(photos[page], page, viewModel)
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PictureBox(
    uri: Uri,
    page: Int,
    viewModel: AddViewModel
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val permission = permission.CAMERA
    val permissionState = rememberMultiplePermissionsState(listOf(permission))

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = compressImage(context, photoUri, compressRate = 0.25)
            when(page) {
                0 -> viewModel.updateFrontUri(photoUri)
                1 -> viewModel.updateBackUri(photoUri)
            }
        }
    }

    if (uri != Uri.EMPTY) {
        Box(
            modifier = Modifier.size(164.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.size(size = 164.dp),
                model = uri,
                contentDescription = null
            )
        }
    }
    else {
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

private fun createImageFile(context: Context): Uri {
    val storageDir: File? = context.getExternalFilesDir(null)
    File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        storageDir
    ).apply {
        return FileProvider.getUriForFile(
            context,
            "cz.cvut.fit.podtacky.provider",
            this
        )
    }
}

private fun compressImage(context: Context, originalIimageUri: Uri, compressRate: Double = 0.5): Uri {
    val compressedImageUri = createImageFile(context)

    val inputStream = context.contentResolver.openInputStream(originalIimageUri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val rotatedBitmap = fixBitmapRotation(context, originalIimageUri, originalBitmap)
    val newW = rotatedBitmap.width * compressRate
    val newH = rotatedBitmap.height * compressRate
    val resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, newW.toInt(), newH.toInt(), true)

    val outputStream = context.contentResolver.openOutputStream(compressedImageUri)!!
    outputStream.use {
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    resizedBitmap.recycle()
    originalBitmap.recycle()
    rotatedBitmap.recycle()

    context.contentResolver.delete(originalIimageUri, null, null)

    return compressedImageUri
}

private fun fixBitmapRotation(context: Context, originalIimageUri: Uri, originalBitmap: Bitmap): Bitmap {
    val exifInputStream = context.contentResolver.openInputStream(originalIimageUri)
    val exif = ExifInterface(exifInputStream!!)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    exifInputStream.close()

   return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
        else -> originalBitmap
    }
}

private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}