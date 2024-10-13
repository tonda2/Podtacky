package cz.tonda2.podtacky.core.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.ThumbVisibility
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoasterList(
    coasters: List<Coaster>,
    emptyText: String,
    modifier: Modifier,
    scrollModifier: Modifier,
    onItemClick: (Coaster) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollAreaState(lazyListState)

    if (coasters.isEmpty()) {
        NoResults(
            text = emptyText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    } else {
        ScrollArea(
            state = scrollState,
            overscrollEffect = null
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = modifier,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(coasters) { coaster ->
                    CoasterCard(
                        coaster = coaster
                    ) {
                        onItemClick(coaster)
                    }
                }
            }
            VerticalScrollbar(
                modifier = scrollModifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 8.dp)
                    .width(4.dp)
            ) {
                Thumb(
                    modifier = Modifier.background(Color.LightGray.copy(0.3f), RoundedCornerShape(100)),
                    thumbVisibility = ThumbVisibility.HideWhileIdle(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        hideDelay = 0.5.seconds
                    )
                )
            }
        }
    }
}