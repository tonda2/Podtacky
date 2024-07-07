package cz.cvut.fit.podtacky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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