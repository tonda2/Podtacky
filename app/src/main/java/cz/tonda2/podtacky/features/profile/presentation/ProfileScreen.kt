package cz.tonda2.podtacky.features.profile.presentation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.presentation.BottomBar
import cz.tonda2.podtacky.features.coaster.presentation.DownloadingScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract(),
        onResult = {}
    )

    if (screenState.downloading) {
        DownloadingScreen(done = screenState.downloadCount)
    }
    else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.profil))
                    }
                )
            },
            bottomBar = {
                BottomBar(navController = navController, isList = false)
            }
        ) { paddingValues ->
            ProfileScreenContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(12.dp)
                    .fillMaxSize(),
                LocalContext.current,
                state = screenState,
                onLoginClick = {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )

                    val signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()

                    launcher.launch(signInIntent)
                },
                onBackupClick = {
                    viewModel.backup()
                },
                onImportClick = {
                    viewModel.import(context)
                },
                onLogoutClick = { viewModel.logOut(context) }
            )
        }
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier,
    context: Context,
    state: ProfileScreenState,
    onLoginClick: () -> Unit,
    onBackupClick: () -> Unit,
    onImportClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (state.name == null) {
            Text(text = stringResource(R.string.please_login), fontWeight = FontWeight.Light)
            Spacer(Modifier.weight(1f))

            Button(
                onClick = { onLoginClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.prihlasit_se))
            }
        } else {
            Text(
                text = stringResource(R.string.jste_prihlaseni_jako),
                style = MaterialTheme.typography.titleMedium
            )

            Text(state.name)

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    onBackupClick()
                    Toast.makeText(
                        context,
                        context.getString(R.string.zaloha_spustena), Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.zalohovat))
            }

            Button(
                onClick = {
                    onImportClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.importovat_z_lohu))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onLogoutClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.odhlasit_se))
            }
        }
    }
}