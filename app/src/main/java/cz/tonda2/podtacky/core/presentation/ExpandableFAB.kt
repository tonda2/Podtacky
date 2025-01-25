package cz.tonda2.podtacky.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableFAB(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    options: List<FABItem> = listOf(),
    onClick: () -> Unit = {},
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    content: @Composable () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
        ) {
            LazyColumn(Modifier.padding(bottom = 8.dp)) {
                items(options.size) {
                    FABListItem(
                        item = options[it],
                        containerColor = containerColor,
                        buttonShape = shape
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        val transition = updateTransition(targetState = expanded, label = "fabClickTransition")
        val rotation by transition.animateFloat(label = "fabIconRotation") {
            if (it) 315f else 0f
        }

        FloatingActionButton(
            onClick = { onClick() },
            modifier = modifier.rotate(rotation),
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            content()
        }
    }
}

@Composable
fun FABListItem(item: FABItem, containerColor: Color, buttonShape: Shape) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.padding(6.dp)) {
            Text(text = item.title)
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(
            onClick = { item.onClick() },
            modifier = Modifier.size(45.dp),
            containerColor = containerColor,
            shape = buttonShape
        ) {
            Icon(imageVector = item.icon, contentDescription = "${item.title} option")
        }
    }
}

data class FABItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)