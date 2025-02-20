package my.edu.utem.gymmembership.data.remote.model

import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("_id") val id: String,
    @SerializedName("plan_id") val planId: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Float,
    @SerializedName("duration") val duration: String, // ✅ Store duration as String
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
