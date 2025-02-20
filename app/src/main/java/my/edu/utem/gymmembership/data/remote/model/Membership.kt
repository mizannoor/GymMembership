package my.edu.utem.gymmembership.data.remote.model

import com.google.gson.annotations.SerializedName
import my.edu.utem.gymmembership.utils.DateUtils

data class Membership(
    @SerializedName("_id") val id: String,
    @SerializedName("membership_id") val membershipId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("plan_id") val planId: String,
    @SerializedName("start_date") private val startDate: String,
    @SerializedName("end_date") private val endDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) {
    fun formattedStartDate(): String = DateUtils.formatDate(startDate)
    fun formattedEndDate(): String = DateUtils.formatDate(endDate)
}
