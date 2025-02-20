package my.edu.utem.gymmembership.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import my.edu.utem.gymmembership.viewmodel.PlanViewModel
import my.edu.utem.gymmembership.data.remote.model.Plan
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.edu.utem.gymmembership.ui.components.SwipeToRevealCardT

@Composable
fun PlanScreen(navController: NavController, planViewModel: PlanViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val plans = remember { mutableStateListOf<Plan>() }
    val scaffoldState = rememberScaffoldState()

    // ✅ Fetch plans automatically when screen loads
    LaunchedEffect(Unit) {
        planViewModel.fetchPlans()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("Membership Plans") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            /*Button(
                onClick = { planViewModel.fetchPlans() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Load Plans")
            }*/

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(plans, key = { it.id }) { plan ->
                    /*Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Plan: ${plan.name}", style = MaterialTheme.typography.h6)
                            Text(text = "Price: \$${plan.price}")
                            // ✅ Extract numeric part from duration (e.g., "1" from "1 month")
                            val numericDuration = plan.duration.split(" ")[0]
                            Text(text = "Duration: $numericDuration months") // ✅ Format properly
                            Text(text = "Created At: ${plan.createdAt}")
                        }
                    }*/
                    SwipeToRevealCardT(
                        item = plan,
                        onDelete = { it ->
                            planViewModel.deletePlan(it.id,
                                onSuccess = { plans.remove(it)
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()},
                                onError = {Log.e("Delete", it)
                                }
                            )
                        }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
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
        }
    }

    // Observe API Response
    planViewModel.plansResult = { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                plans.clear()
                plans.addAll(it)
            }
        }
    }
}
