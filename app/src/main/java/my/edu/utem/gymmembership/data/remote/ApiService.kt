package my.edu.utem.gymmembership.data.remote

import MembershipStatusUpdateRequest
import my.edu.utem.gymmembership.data.remote.model.LoginRequest
import my.edu.utem.gymmembership.data.remote.model.LoginResponse
import my.edu.utem.gymmembership.data.remote.model.Membership
import my.edu.utem.gymmembership.data.remote.model.MembershipCreateRequest
import my.edu.utem.gymmembership.data.remote.model.Payment
import my.edu.utem.gymmembership.data.remote.model.PaymentCreateRequest
import my.edu.utem.gymmembership.data.remote.model.PaymentStatusUpdateRequest
import my.edu.utem.gymmembership.data.remote.model.Plan
import my.edu.utem.gymmembership.data.remote.model.RegisterRequest
import my.edu.utem.gymmembership.data.remote.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @POST("login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registration: RegisterRequest): Response<RegisterResponse>

    @GET("payments")
    suspend fun fetchPayments(): Response<List<Payment>>

    @POST("payments")
    suspend fun createPayment(@Body newPayment: PaymentCreateRequest): Response<Payment>

    @DELETE("payments/{id}")
    suspend fun deletePayment(@Path("id") paymentId: String): Response<Unit>

    @PUT("payments/{id}")
    suspend fun updatePaymentStatus(
        @Path("id") paymentId: String,
        @Body request: PaymentStatusUpdateRequest
    ): Response<Payment>

    // For endpoints on port 4000, you might need a separate Retrofit instance
    @GET("plans")
    suspend fun fetchPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun fetchPlanById(@Path("id") planId: String): Response<Plan>

    @DELETE("plans/{id}")
    suspend fun deletePlan(@Path("id") planId: String): Response<Unit>

    @DELETE("memberships/{id}")
    suspend fun deleteMembership(@Path("id") membershipId: String): Response<Unit>

    @POST("memberships")
    suspend fun createMembership(@Body request: MembershipCreateRequest): Response<Membership>

    @GET("memberships")
    suspend fun fetchMemberships(): Response<List<Membership>>

    @PUT("memberships/{id}")
    suspend fun updateMembershipStatus(
        @Path("id") membershipId: String,
        @Body request: MembershipStatusUpdateRequest
    ): Response<Membership>

}