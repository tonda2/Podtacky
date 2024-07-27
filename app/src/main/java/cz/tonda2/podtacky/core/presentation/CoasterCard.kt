package cz.tonda2.podtacky.core.presentation

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.features.coaster.domain.Coaster

@Composable
fun CoasterCard(
    coaster: Coaster,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        CoasterItem(coaster,
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .semantics {
                    contentDescription = context.getString(
                        R.string.a_single_coaster_description,
                        coaster.description
                    )
                }
        )
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
                model = if (coaster.frontUri != Uri.EMPTY) coaster.frontUri else coaster.backUri,
                contentDescription = stringResource(R.string.coaster_item_image)
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