package cz.tonda2.podtacky.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E3A8A),
    secondary = Color(0xFF9595FE),
    primaryContainer = Color(0xFF181819),
    secondaryContainer = Color(0xFF2E2E2F),
    tertiaryContainer = Color(0xFF474747),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF8B8B8C),
    onTertiary = Color(0xFF5D5D5E),
    outlineVariant = Color(0xFF5D5D5E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EA),
    primaryContainer = Color(0xFFF4F4F9),
    secondaryContainer = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE7E7EC),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF6200EA),
    onSecondary = Color(0xFF333333),
    onTertiary = Color(0xFF666666),
    outlineVariant = Color(0xFFCCCCCC),
)

@Composable
fun PodtackyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colorScheme = if (darkTheme) {
//        DarkColorScheme
//    }
//    else {
//        LightColorScheme
//    }

    val colorScheme = DarkColorScheme

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = colorScheme.primaryContainer
    )
    systemUiController.setStatusBarColor(
        color = colorScheme.primaryContainer
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}