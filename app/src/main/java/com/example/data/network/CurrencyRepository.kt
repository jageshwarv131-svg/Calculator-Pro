package com.example.data.network

import com.example.data.model.FALLBACK_RATES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class RatesResult(
    val rates: Map<String, Double>,
    val lastUpdateTime: String,
    val isOnline: Boolean
)

class CurrencyRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun fetchRates(): RatesResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://open.er-api.com/v6/latest/USD")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    val json = JSONObject(bodyString)
                    val ratesObj = json.optJSONObject("rates")
                    if (ratesObj != null) {
                        val map = mutableMapOf<String, Double>()
                        val keys = ratesObj.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            map[key] = ratesObj.optDouble(key, 1.0)
                        }

                        val timeFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
                        val formattedTime = timeFormat.format(Date())

                        return@withContext RatesResult(
                            rates = map,
                            lastUpdateTime = formattedTime,
                            isOnline = true
                        )
                    }
                }
            }
        } catch (_: Exception) {
            // Network failure fallback
        }

        RatesResult(
            rates = FALLBACK_RATES,
            lastUpdateTime = "Offline",
            isOnline = false
        )
    }
}
