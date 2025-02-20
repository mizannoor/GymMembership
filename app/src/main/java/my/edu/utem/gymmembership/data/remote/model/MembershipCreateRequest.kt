package my.edu.utem.gymmembership.data.remote.model

data class MembershipCreateRequest(
    val user_id: String,
    val plan_id: String,
    val start_date: String,
    val end_date: String,
    val status: String
)
