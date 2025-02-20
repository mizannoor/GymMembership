package my.edu.utem.gymmembership.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.data.remote.model.Plan
import my.edu.utem.gymmembership.data.repository.PlanRepository
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {


    private val _selectedPlan = MutableStateFlow<Plan?>(null)
    val selectedPlan: StateFlow<Plan?> = _selectedPlan.asStateFlow()
    var plansResult: ((Response<List<Plan>>) -> Unit)? = null

    init {
//        fetchPlans()
    }

    fun fetchPlans() {
        viewModelScope.launch {
            val response = planRepository.fetchPlans()
            Log.d("PlanViewModel", "API Response: ${response.body()?.size} plans fetched")
            plansResult?.invoke(response)
        }
    }


    fun deletePlan(planId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = planRepository.deletePlan(planId)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Failed to delete plan")
            }
        }
    }

    fun fetchPlanById(planId: String) {
        viewModelScope.launch {
            val plan = planRepository.getPlanById(planId)
            _selectedPlan.value = plan
        }
    }

    fun getPlanById(planId: String) {
        viewModelScope.launch {
            try {
                val response = planRepository.getPlanById(planId)
                _selectedPlan.value = response
            } catch (e: Exception) {
                Log.e("PlanViewModel", "Error fetching plan by ID: ${e.message}")
            }
        }
    }
}
