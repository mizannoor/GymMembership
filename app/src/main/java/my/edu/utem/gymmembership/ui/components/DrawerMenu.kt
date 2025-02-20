package my.edu.utem.gymmembership.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.utem.gymmembership.ui.navigation.Screen

@Composable
fun DrawerMenu(navController: NavController, closeDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Gym Membership App",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h6
        )

        Divider(color = Color.Gray)

        DrawerItem("Membership", Screen.Membership.route, navController, closeDrawer)
        DrawerItem("Plans", Screen.Plans.route, navController, closeDrawer)
        DrawerItem("Payments", Screen.Payments.route, navController, closeDrawer)
        DrawerItem("Logout", Screen.Login.route, navController, closeDrawer)
    }
}

@Composable
fun DrawerItem(title: String, route: String, navController: NavController, closeDrawer: () -> Unit) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(route) { launchSingleTop = true }
                closeDrawer()
            }
            .padding(16.dp)
    )
}
