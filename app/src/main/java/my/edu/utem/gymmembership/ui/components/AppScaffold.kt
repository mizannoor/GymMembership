package my.edu.utem.gymmembership.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.ui.navigation.Screen
import my.edu.utem.gymmembership.ui.screen.*
import my.edu.utem.gymmembership.viewmodel.PlanViewModel

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // ✅ Track the current destination
    val currentDestination by navController.currentBackStackEntryAsState()

    val shouldShowDrawer = currentDestination?.destination?.route != Screen.Login.route

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Gym Membership App") },
                navigationIcon = {
                    if (shouldShowDrawer) { // ✅ Show menu only if NOT on Login screen
                        IconButton(onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                }
            )
        },
        drawerContent = {
            if (shouldShowDrawer) { // ✅ Hide drawer when on Login screen
                DrawerMenu(navController) { coroutineScope.launch { scaffoldState.drawerState.close() } }
            }
        }
    ) { paddingValues ->
        NavHost(navController, startDestination = Screen.Login.route, Modifier.padding(paddingValues)) {
            composable(Screen.Login.route) { LoginScreen(navController) } // ✅ Pass NavController to LoginScreen
            composable(Screen.Membership.route) { MembershipScreen(navController) }
            composable(Screen.Plans.route) { PlanScreen(navController) }
            composable(Screen.Payments.route) { PaymentScreen(navController) }

            // ✅ Add navigation for Plan Details Screen
            composable(
                route = Screen.PlanDetails.route,
                arguments = listOf(navArgument("planId") { type = NavType.StringType })
            ) { backStackEntry ->
                val planViewModel: PlanViewModel = hiltViewModel()
                val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
                LaunchedEffect(planId) {
                    planViewModel.fetchPlanById(planId)
                }
                val plan = planViewModel.selectedPlan.collectAsState().value

                plan?.let {
                    PlanDetailScreen(navController, plan.id)
                }?: run {
                    // Optionally show loading or error UI
                    Text("Loading plan details...")
                }
            }

        }
    }
}
