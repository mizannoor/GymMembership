package my.edu.utem.gymmembership.ui.navigation

import my.edu.utem.gymmembership.data.remote.model.Plan

sealed class Screen(val route: String) {
    object Membership : Screen("membership")
    object Plans : Screen("plans")
    object Payments : Screen("payments")
    object Login : Screen("login")

    // ✅ Add Plan Details Route with Arguments
    object PlanDetails : Screen("plan_detail/{planId}") {
        fun createRoute(planId: String): String {
            return "plan_detail/$planId"
        }
    }
}
