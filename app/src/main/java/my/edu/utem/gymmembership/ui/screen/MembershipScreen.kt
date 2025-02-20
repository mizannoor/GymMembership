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
import my.edu.utem.gymmembership.viewmodel.MembershipViewModel
import my.edu.utem.gymmembership.data.remote.model.Membership
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.edu.utem.gymmembership.data.remote.model.Plan
import my.edu.utem.gymmembership.ui.components.PlanSelectionDialog
import my.edu.utem.gymmembership.ui.components.SwipeToRevealCardT
import my.edu.utem.gymmembership.ui.navigation.Screen
import my.edu.utem.gymmembership.viewmodel.PlanViewModel

@Composable
fun MembershipScreen(navController: NavController, membershipViewModel: MembershipViewModel = hiltViewModel(), planViewModel: PlanViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val memberships = remember { mutableStateListOf<Membership>() }
    val scaffoldState = rememberScaffoldState()
    val plans = remember { mutableStateListOf<Plan>() }
    var showPlanDialog by remember { mutableStateOf(false) } // ✅ Controls plan selection dialog

    // ✅ Fetch data automatically when screen loads
    LaunchedEffect(Unit) {
        Log.d("MembershipScreen", "Fetching memberships...")
        membershipViewModel.fetchMemberships()
        Log.d("MembershipScreen", "Fetching plans...")
        planViewModel.fetchPlans()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("My Memberships") }) },
        bottomBar = {
            Button(
                onClick = { showPlanDialog = true }, // ✅ Show plan selection popup
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Renew Membership")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            /*Button(
                onClick = { membershipViewModel.fetchMemberships() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Load Memberships")
            }*/

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(memberships, key = { it.id }) { membership ->
                    /*Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Membership Id: ${membership.membershipId}", style = MaterialTheme.typography.h6)
                            Text(text = "Status: ${membership.status}")
                            Text(text = "Start Date: ${membership.formattedStartDate()}")
                            Text(text = "End Date: ${membership.formattedEndDate()}")
                        }
                    }*/
                    SwipeToRevealCardT(
                        item = membership,
                        onDelete = {it ->
                            membershipViewModel.deleteMembership(it.id,
                                onSuccess = {
                                    memberships.remove(it)
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show() },
                                onError = {
                                    Log.e("Delete", it) }
                            )
                        }
                    ){
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Membership Id: ${membership.membershipId}", style = MaterialTheme.typography.h6)
                                Text(text = "Status: ${membership.status}")
                                Text(text = "Start Date: ${membership.formattedStartDate()}")
                                Text(text = "End Date: ${membership.formattedEndDate()}")
                            }
                        }
                    }
                }
            }
        }
    }

    // ✅ Show Plan Selection Dialog when "Renew Membership" is clicked
    if (showPlanDialog) {
        PlanSelectionDialog(
            plans = plans,
            onDismiss = { showPlanDialog = false },
            onPlanSelected = { selectedPlan ->
                Log.d("RenewMembership", "Selected Plan: ${selectedPlan.name}")
                showPlanDialog = false // ✅ Close dialog
                navController.navigate("plan_detail/${selectedPlan.id}")
            }
        )
    }

    // Observe API Response
    membershipViewModel.membershipsResult = { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                memberships.clear()
                memberships.addAll(it)
            }
        }
    }

    planViewModel.plansResult = { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                plans.clear()
                plans.addAll(it) // ✅ Ensure plans are added to the list
            }
        }
    }
}
