package my.edu.utem.gymmembership.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.viewmodel.MembershipViewModel
import my.edu.utem.gymmembership.viewmodel.PaymentViewModel
import my.edu.utem.gymmembership.viewmodel.PlanViewModel
import kotlinx.datetime.*
import kotlinx.datetime.plus // import extension for LocalDateTime
import my.edu.utem.gymmembership.data.remote.model.Membership
import my.edu.utem.gymmembership.ui.navigation.Screen
import my.edu.utem.gymmembership.utils.DateUtils
import java.util.Date
import java.util.UUID

@Composable
fun PlanDetailScreen(navController: NavController, planId: String, paymentViewModel: PaymentViewModel = hiltViewModel(),     membershipViewModel: MembershipViewModel = hiltViewModel()) {
    val viewModel: PlanViewModel = hiltViewModel()
    val scaffoldState = rememberScaffoldState() // For Snackbar
    // Retrieve user session info, etc.
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(planId) {
        viewModel.fetchPlanById(planId)
    }

    val selectedPlan by viewModel.selectedPlan.collectAsState()
    // ✅ State for showing Payment Dialog
    // PaymentMethodDialog’s onProceed references handlePayment
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }

    // Overlay states
    var showOverlay by remember { mutableStateOf(false) }
    var overlayText by remember { mutableStateOf("") }

    selectedPlan?.let { plan ->
        Text(text = "Plan Name: ${plan.name}")
    } ?: Text(text = "Loading...")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { showPaymentDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Pay")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Card to contain plan details
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Plan Details", style = MaterialTheme.typography.h5)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedPlan == null) {
                        Text(text = "Loading...")
                    } else {
                        Text(text = "Name: ${selectedPlan?.name}")
                        Text(text = "Price: $${selectedPlan?.price}")
                        Text(text = "Duration: ${selectedPlan?.duration}")
                    }
                }
            }
        }
    }

    // PaymentMethodDialog’s onProceed calls handlePayment
    fun handlePayment(chosenMethod: String) {
        println("chosenMethod: $chosenMethod")
        coroutineScope.launch {
            val user = paymentViewModel.getCurrentUser()
            var newMembership: Membership? = null

            // 1) Show overlay loading text "Authorization request"
            overlayText = "Authorization request"
            showOverlay = true
            delay(2000)
            println("selectedPlan?.duration: ${selectedPlan?.duration}")
            val dateTimeUnit = if (selectedPlan?.duration == "1 year") {
                DateTimeUnit.YEAR
            } else {
                DateTimeUnit.MONTH
            }
            val currentInstant = Clock.System.now()
            val newInstant = currentInstant.plus(1, dateTimeUnit, TimeZone.currentSystemDefault())
            val currentDate = Date.from(currentInstant.toJavaInstant())
            val newDate = Date.from(newInstant.toJavaInstant())
            val membershipResponse = membershipViewModel.createMembershipForUser(
                planId = planId,
                startDate = DateUtils.toApiFormat(currentDate), // e.g. "2025-02-20T01:14:06.158Z"
                endDate = DateUtils.toApiFormat(newDate),
                status = "Inactive"
            )

            if (membershipResponse?.isSuccessful == true) {
                newMembership = membershipResponse.body()
                // ✅ You now have the newly created membership in 'newMembership'
                // For example, show a log or toast
                Log.d("PlanDetailScreen", "Membership created: ${newMembership?.id}")
            } else {
                // handle error
                Log.e("PlanDetailScreen", "Failed to create membership: ${membershipResponse?.errorBody()}")
            }
            // 2) Send create Payment API request
            val paymentCreatedResponse = selectedPlan?.price?.let {
                newMembership?.let { it1 ->
                    paymentViewModel.paymentRepository.createPayment(
                        paymentId = UUID.randomUUID().toString(),
                        userId = user?.userId ?: "",
                        membershipId = it1.id,
                        amount = it, // If plan.price is a Float, convert to Double
                        date =  java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
                            .format(java.util.Date()),
                        status = "Pending"
                    )
                }
            }
            if (paymentCreatedResponse?.isSuccessful == true) {
                val createdPayment = paymentCreatedResponse.body()
                // 3) Update overlay text to "Authorized"
                overlayText = "Authorized"
                delay(2000)

                // 4) Update overlay text to "Waiting merchant respond"
                overlayText = "Waiting merchant respond"
                delay(2000)

                // 5) Now update the payment status to "Paid"
                if (createdPayment != null) {
                    val updateResponse = paymentViewModel.paymentRepository.updatePaymentStatus(createdPayment.id, "Paid")
                    if (updateResponse.isSuccessful) {
                        overlayText = "Finalizing transaction"
                        delay(2000)

                        // ✅ Now update the membership status to "Active"
                        if (newMembership != null) {
                            val membershipUpdateResp = membershipViewModel.updateMembershipStatus(newMembership.id, "Active")
                            if (membershipUpdateResp.isSuccessful) {
                                Log.d("PlanDetailScreen", "Membership updated to Active")
                                // You could display a log, toast, or proceed with next steps
                            } else {
                                Log.e("PlanDetailScreen", "Failed to update membership status: ${membershipUpdateResp.errorBody()}")
                            }
                        }
                    } else {
                        // Handle update failure
                        overlayText = "Error updating payment status"
                        delay(2000)
                    }
                } else {
                    overlayText = "Payment creation error"
                    delay(2000)
                }

                // 6) Close overlay
                showOverlay = false

                // 7) Show success popup
                showSuccessPopup = true
                delay(3000)
                // 8) Navigate back to Membership screen
                /*navController.navigate(Screen.Membership.route) {
                    popUpTo(Screen.Membership.route) { inclusive = false }
                }*/
            } else {
                // Handle create payment error
                overlayText = "Payment creation failed"
                delay(2000)
                showOverlay = false
            }
        }
    }

    // ✅ Payment Dialog
    if (showPaymentDialog) {
        PaymentMethodDialog(
            onDismiss = { showPaymentDialog = false },
            onProceed = { chosenMethod ->
                // TODO: Implement Payment logic
                // e.g., navController.navigate("payment_summary")
                showPaymentDialog = false
                handlePayment(chosenMethod) // Payment flow
            }
        )
    }


    // 7) Show success message via Snackbar
    if (showSuccessPopup) {
        SuccessDialog(
            onDismiss = {
                showSuccessPopup = false
                // 8) Navigate back to Membership screen
                navController.navigate(Screen.Membership.route) {
                    popUpTo(Screen.Membership.route) { inclusive = false }
                }
            }
        )
    }

    // Loading Overlay
    if (showOverlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = overlayText, color = Color.White)
            }
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Payment successful") },
        text = { Text("Thank you! Membership has been renewed.") },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

// ✅ PaymentMethodDialog composable
@Composable
fun PaymentMethodDialog(
    onDismiss: () -> Unit,
    onProceed: (String) -> Unit
) {
    // Payment method list
    val paymentMethods = listOf("Credit Card", "PayPal", "Bank Transfer")

    // State to hold selected method
    var selectedMethod by remember { mutableStateOf(paymentMethods[0]) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select Payment Method") },
        text = {
            Column {
                // Styled radio buttons for each payment method
                paymentMethods.forEach { method ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (method == selectedMethod),
                            onClick = { selectedMethod = method },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colors.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = method)
                    }
                }
            }
        },
        confirmButton = {
            Row {
                Button(
                    onClick = { onProceed(selectedMethod) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Proceed Payment")
                }
                OutlinedButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        }
    )
}

