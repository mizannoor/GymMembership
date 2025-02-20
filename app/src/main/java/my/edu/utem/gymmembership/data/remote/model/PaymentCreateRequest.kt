package my.edu.utem.gymmembership.data.remote.model

data class PaymentCreateRequest(
    val payment_id: String,
    val user_id: String,
    val membership_id: String,
    val amount: Float,
    val date: String,
    val status: String
)
