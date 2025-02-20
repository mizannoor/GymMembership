package my.edu.utem.gymmembership.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.data.local.entity.UserSession
import my.edu.utem.gymmembership.data.remote.model.Payment
import my.edu.utem.gymmembership.data.repository.PaymentRepository
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
     val paymentRepository: PaymentRepository,
     val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var paymentsResult: ((Response<List<Payment>>) -> Unit)? = null

    suspend fun getCurrentUser(): UserSession? {
        return userSessionRepository.getUserSession()
    }

    fun fetchPayments() {
        viewModelScope.launch {
            val response = paymentRepository.fetchPayments()
            paymentsResult?.invoke(response)
        }
    }

    fun deletePayment(paymentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = paymentRepository.deletePayment(paymentId)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Failed to delete payment")
            }
        }
    }
}
