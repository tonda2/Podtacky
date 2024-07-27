package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MultiLineText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    maxLength: Int = 30,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val words = text.split(' ')
    var line = ""

    Column {
        for (word in words) {
            line = if (line.length + word.length < maxLength) {
                line.plus(word).plus(' ')
            } else {
                Text(
                    modifier = modifier,
                    text = line,
                    style = style,
                    color = color,
                    fontWeight = fontWeight
                )
                word.plus(' ')
            }
        }
        if (line.isNotEmpty()) {
            Text(
                modifier = modifier,
                text = line,
                style = style,
                color = color,
                fontWeight = fontWeight
            )
        }
    }
}