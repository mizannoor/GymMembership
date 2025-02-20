package my.edu.utem.gymmembership.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.data.remote.model.Membership
import my.edu.utem.gymmembership.data.repository.MembershipRepository
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MembershipViewModel @Inject constructor(
    private val membershipRepository: MembershipRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var membershipsResult: ((Response<List<Membership>>) -> Unit)? = null

    suspend fun createMembershipForUser(
        planId: String,
        startDate: String,
        endDate: String,
        status: String
    ): Response<Membership>? {
        val userId = userSessionRepository.getUserSession()?.userId ?: return null
        return membershipRepository.createMembership(
            userId = userId,
            planId = planId,
            startDate = startDate,
            endDate = endDate,
            status = status
        )
    }

    suspend fun updateMembershipStatus(membershipId: String, status: String): Response<Membership> {
        return membershipRepository.updateMembershipStatus(membershipId, status)
    }

    fun fetchMemberships() {
        viewModelScope.launch {
            val response = membershipRepository.fetchMemberships()
            membershipsResult?.invoke(response)
        }
    }

    fun deleteMembership(membershipId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = membershipRepository.deleteMembership(membershipId)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Failed to delete membership")
            }
        }
    }
}
