package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.tonda2.podtacky.R

@Composable
fun NoResults(
    text: String,
    emoji: String = stringResource(R.string.beer_glass_emoji),
    style: TextStyle,
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(Alignment.CenterHorizontally, unbounded = true),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = emoji,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultiLineText(
                text = text,
                style = style,
                color = color,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
@Preview
fun NoResultsPreview() {
    NoResults(text = "Zatím nemáte žádné podtácky, přidejte si je tlačítkem +", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
}