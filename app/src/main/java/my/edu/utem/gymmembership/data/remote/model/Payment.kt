package my.edu.utem.gymmembership.data.remote.model

import com.google.gson.annotations.SerializedName
import my.edu.utem.gymmembership.utils.DateUtils

data class Payment(
    @SerializedName("_id") val id: String,
    @SerializedName("payment_id") val paymentId: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("date") private val date: String,  // Raw API Date
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") private val createdAt: String, // Raw API Date
    @SerializedName("updatedAt") private val updatedAt: String  // Raw API Date
) {
    fun formattedDate(): String = DateUtils.formatDate(date) // ✅ Format date for display
    fun formattedCreatedAt(): String = DateUtils.formatDate(createdAt)
    fun formattedUpdatedAt(): String = DateUtils.formatDate(updatedAt)
}
