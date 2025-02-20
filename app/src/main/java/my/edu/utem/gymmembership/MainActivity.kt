package my.edu.utem.gymmembership

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import my.edu.utem.gymmembership.ui.components.AppScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScaffold() // ✅ Loads Navigation Drawer + Screens
        }
    }
}
