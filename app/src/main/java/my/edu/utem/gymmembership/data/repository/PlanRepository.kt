package my.edu.utem.gymmembership.data.repository

import my.edu.utem.gymmembership.data.remote.ApiService
import my.edu.utem.gymmembership.data.remote.model.Plan
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class PlanRepository @Inject constructor(@Named("CrudApi") private val apiService: ApiService) {
    suspend fun fetchPlans(): Response<List<Plan>> {
        return apiService.fetchPlans()
    }

    suspend fun deletePlan(planId: String): Response<Unit> {
        return apiService.deletePlan(planId)
    }

    suspend fun getPlanById(planId: String): Plan? {
        val response = apiService.fetchPlanById(planId)
        return if (response.isSuccessful) response.body() else null
    }
}
