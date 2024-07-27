package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cz.tonda2.podtacky.R

@Composable
fun PageIndicator(
    count: Int,
    current: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            repeat(count) { iteration ->
                val color =
                    if (current == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                        .semantics {
                            contentDescription =
                                context.getString(R.string.page_indicator_current_page, iteration.toString())
                        }
                )
            }
        }
    }
}