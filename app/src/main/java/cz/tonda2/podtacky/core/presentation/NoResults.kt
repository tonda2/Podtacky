package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoResults(
    text: String,
    style: TextStyle,
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
//            Text(
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                text = "\uD83D\uDE1E",
//                fontSize = 32.sp
//            )
//            Spacer(modifier = Modifier.height(8.dp))
            MultiLineText(
                text = text,
                style = style,
                color = color,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
@Preview
fun NoResultsPreview() {
    NoResults(text = "Zatím nemáte žádné podtácky, přidejte si je tlačítkem +", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
}