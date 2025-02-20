package my.edu.utem.gymmembership.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import my.edu.utem.gymmembership.viewmodel.PaymentViewModel
import my.edu.utem.gymmembership.data.remote.model.Payment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.edu.utem.gymmembership.ui.components.SwipeToRevealCardT

@Composable
fun PaymentScreen(navController: NavController, paymentViewModel: PaymentViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val payments = remember { mutableStateListOf<Payment>() }
    val scaffoldState = rememberScaffoldState()

    // ✅ Fetch payments automatically when screen loads
    LaunchedEffect(Unit) {
        paymentViewModel.fetchPayments()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("Payment History") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            /*Button(
                onClick = { paymentViewModel.fetchPayments() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Load Payments")
            }*/

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(payments, key = { it.id }) { payment ->
                    /*Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Amount: \$${payment.amount}", style = MaterialTheme.typography.h6)
                            Text(text = "Date: ${payment.formattedDate()}") // ✅ Use formatted date
                            Text(text = "Status: ${payment.status}")
                        }
                    }*/
                    SwipeToRevealCardT(
                        item = payment,
                        onDelete = { it ->
                            paymentViewModel.deletePayment(it.id,
                                onSuccess = {
                                    payments.remove(it)
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                                            },
                                onError = {
                                    Log.e("Delete", it)
                                }
                            )
                        }
                    ){
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Amount: \$${payment.amount}", style = MaterialTheme.typography.h6)
                                Text(text = "Date: ${payment.formattedDate()}")
                                Text(text = "Status: ${payment.status}")
                            }
                        }
                    }
                }
            }
        }
    }

    // Observe API Response
    paymentViewModel.paymentsResult = { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                payments.clear()
                payments.addAll(it)
            }
        }
    }
}
