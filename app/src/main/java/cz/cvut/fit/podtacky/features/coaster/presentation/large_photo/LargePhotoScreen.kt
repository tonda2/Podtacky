package cz.cvut.fit.podtacky.features.coaster.presentation.large_photo

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.cvut.fit.podtacky.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LargePhotoScreen(
    navController: NavController,
    viewModel: LargePhotoViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    screenState.coaster?.let { coaster ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalPager(
                state = rememberPagerState(
                    pageCount = { 2 }
                )
            ) { page ->
                val uri = when (page) {
                    0 -> coaster.frontUri
                    1 -> coaster.backUri
                    else -> Uri.EMPTY
                }

                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                modifier = Modifier
                    .size(64.dp)
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .clickable { navController.navigateUp() },
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}