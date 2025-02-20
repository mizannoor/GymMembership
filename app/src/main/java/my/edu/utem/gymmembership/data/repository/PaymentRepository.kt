package my.edu.utem.gymmembership.data.repository

import my.edu.utem.gymmembership.data.remote.ApiService
import my.edu.utem.gymmembership.data.remote.model.Payment
import my.edu.utem.gymmembership.data.remote.model.PaymentCreateRequest
import my.edu.utem.gymmembership.data.remote.model.PaymentStatusUpdateRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class PaymentRepository @Inject constructor(@Named("CrudApi") private val apiService: ApiService) {
    suspend fun createPayment(
        paymentId: String,
        userId: String,
        membershipId: String,
        amount: Float,
        date: String,
        status: String
    ): Response<Payment> {
        val requestBody = PaymentCreateRequest(
            payment_id = paymentId,
            user_id = userId,
            membership_id = membershipId,
            amount = amount,
            date = date,
            status = status
        )
        return apiService.createPayment(requestBody)
    }

    suspend fun fetchPayments(): Response<List<Payment>> {
        return apiService.fetchPayments()
    }

    suspend fun deletePayment(paymentId: String): Response<Unit> {
        return apiService.deletePayment(paymentId)
    }

    suspend fun updatePaymentStatus(paymentId: String, status: String): Response<Payment> {
        val request = PaymentStatusUpdateRequest(status)
        return apiService.updatePaymentStatus(paymentId, request)
    }

}
