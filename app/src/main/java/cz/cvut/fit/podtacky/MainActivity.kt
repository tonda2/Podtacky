package cz.cvut.fit.podtacky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cz.cvut.fit.podtacky.core.presentation.Navigation
import cz.cvut.fit.podtacky.core.presentation.theme.PodtackyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodtackyTheme {
                Navigation()
            }
        }
    }
}