package my.edu.utem.gymmembership.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import my.edu.utem.gymmembership.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.edu.utem.gymmembership.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("mizannoor92@yahoo.com") }
    var password by remember { mutableStateOf("12345") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // ✅ Move to top
        horizontalAlignment = Alignment.CenterHorizontally // ✅ Center horizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp)) // ✅ Add space from top

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth() // ✅ Full width
        )

        Spacer(modifier = Modifier.height(8.dp)) // ✅ Space between fields

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth() // ✅ Full width
        )

        Spacer(modifier = Modifier.height(16.dp)) // ✅ Space before button

        Button(
            onClick = { authViewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth() // ✅ Full width
        ) {
            Text("Login")
        }
    }

    authViewModel.loginResult = { response ->
        if (response.isSuccessful) {
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Membership.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        } else {
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }
}

