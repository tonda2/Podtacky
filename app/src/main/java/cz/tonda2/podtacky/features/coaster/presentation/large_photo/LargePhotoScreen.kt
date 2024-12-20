package cz.tonda2.podtacky.features.coaster.presentation.large_photo

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
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.PageIndicator
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
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
            val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = screenState.startIndex)
            val zoomState = rememberZoomState(maxScale = 3f)

            HorizontalPager(
                state = pagerState
            ) { page ->
                val uri = when (page) {
                    0 -> coaster.frontUri
                    1 -> coaster.backUri
                    else -> Uri.EMPTY
                }

                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(R.string.large_coaster_image, page),
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(
                            zoomState = zoomState,
                            scrollGesturePropagation = ScrollGesturePropagation.NotZoomed
                        )
                )
            }

            PageIndicator(count = pagerState.pageCount, current = pagerState.currentPage)

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