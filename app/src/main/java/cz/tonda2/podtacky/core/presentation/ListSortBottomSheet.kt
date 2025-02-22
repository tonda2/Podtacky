package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSortBottomSheet(
    onDismissRequest: () -> Unit,
    selected: Int,
    options: List<CoasterSortType>,
    onOptionClick: (CoasterSortType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        ListSortBottomSheetContent(
            options = options,
            selected,
            onOptionClick = { order ->
                onOptionClick(order)
            }
        )
    }
}

@Composable
fun ListSortBottomSheetContent(
    options: List<CoasterSortType>,
    selected: Int,
    onOptionClick: (CoasterSortType) -> Unit
) {
    val selectedIndexState by remember { mutableIntStateOf(selected) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sort),
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp
        )

        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        onOptionClick(option)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (index == selectedIndexState) MaterialTheme.colorScheme.secondary else Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                if (index == selectedIndexState) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.selected),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}