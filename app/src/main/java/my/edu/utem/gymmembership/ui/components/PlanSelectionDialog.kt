package my.edu.utem.gymmembership.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import my.edu.utem.gymmembership.data.remote.model.Plan

@Composable
fun PlanSelectionDialog(
    plans: List<Plan>,
    onDismiss: () -> Unit,
    onPlanSelected: (Plan) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select a Plan",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(bottom = 16.dp) // ✅ Adds proper spacing below the title
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 400.dp)
                ) {
                    items(plans, key = { it.id }) { plan ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onPlanSelected(plan) },
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Plan: ${plan.name}", style = MaterialTheme.typography.h6)
                                Text(text = "Price: \$${plan.price}")
                                Text(text = "Duration: ${plan.duration}")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
