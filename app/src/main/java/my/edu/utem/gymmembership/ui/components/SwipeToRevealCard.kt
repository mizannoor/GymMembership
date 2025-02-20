package my.edu.utem.gymmembership.ui.components

import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.foundation.rememberRevealState
import androidx.wear.compose.material.*
import androidx.wear.compose.material.SwipeToRevealDefaults

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun <T> SwipeToRevealCardT(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable () -> Unit
) {
    val revealState = rememberRevealState()

    SwipeToRevealCard(
        revealState = revealState,
        primaryAction = {
            SwipeToRevealPrimaryAction(
                revealState = revealState,
                icon = { Icon(SwipeToRevealDefaults.Delete, contentDescription = "Delete", modifier =
                Modifier.size(CardDefaults.AppImageSize)
                    .wrapContentSize(align = Alignment.Center),) },
                label = { Text("Delete") },
                onClick = { onDelete(item) } // ✅ Only delete when button is clicked
            )
        },
        onFullSwipe = { /* Handle full swipe action (optional) */ }
    ) {
        content() // ✅ Display the card content
    }
}
