package my.edu.utem.gymmembership.data.repository

import MembershipStatusUpdateRequest
import my.edu.utem.gymmembership.data.remote.ApiService
import my.edu.utem.gymmembership.data.remote.model.Membership
import my.edu.utem.gymmembership.data.remote.model.MembershipCreateRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class MembershipRepository @Inject constructor(@Named("CrudApi") private val apiService: ApiService) {
    suspend fun createMembership(
        userId: String,
        planId: String,
        startDate: String,
        endDate: String,
        status: String
    ): Response<Membership> {
        val requestBody = MembershipCreateRequest(
            user_id = userId,
            plan_id = planId,
            start_date = startDate,
            end_date = endDate,
            status = status
        )
        return apiService.createMembership(requestBody)
    }


    suspend fun fetchMemberships(): Response<List<Membership>> {
        return apiService.fetchMemberships()
    }

    suspend fun deleteMembership(membershipId: String): Response<Unit> {
        return apiService.deleteMembership(membershipId)
    }

    suspend fun updateMembershipStatus(membershipId: String, newStatus: String): Response<Membership> {
        val request = MembershipStatusUpdateRequest(newStatus)
        return apiService.updateMembershipStatus(membershipId, request)
    }

}
