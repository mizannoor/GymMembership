package my.edu.utem.gymmembership.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    // ✅ Convert API date format to "DD/MMM/YYYY HH:mm a"
    fun formatDate(dateString: String): String {
        return try {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()) // API format
            apiFormat.timeZone = TimeZone.getTimeZone("UTC") // API uses UTC time

            val date = apiFormat.parse(dateString) // Parse raw date
            val outputFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.getDefault()) // Desired format
            outputFormat.format(date!!) // Format to readable date
        } catch (e: Exception) {
            "Invalid Date" // Handle parsing errors
        }
    }

    // New function for writing to server in ISO format
    fun toApiFormat(date: Date): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC") // match server's UTC
        return isoFormat.format(date)
    }
}
