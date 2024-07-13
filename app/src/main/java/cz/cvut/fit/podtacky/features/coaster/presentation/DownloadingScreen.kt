package cz.cvut.fit.podtacky.features.coaster.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.cvut.fit.podtacky.R

@Composable
fun DownloadingScreen(
    done: Int
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.prob_h_stahov_n),
        )
        Text(
            text = stringResource(R.string.sta_eno_z_podt_ck, done),
        )
    }
}