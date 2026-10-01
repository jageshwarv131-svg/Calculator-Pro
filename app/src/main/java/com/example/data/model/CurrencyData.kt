package com.example.data.model

data class Currency(
    val code: String,
    val name: String,
    val flag: String
)

val DEFAULT_CURRENCIES = listOf(
    Currency("USD", "United States Dollar", "🇺🇸"),
    Currency("INR", "Indian Rupee", "🇮🇳"),
    Currency("EUR", "Euro", "🇪🇺"),
    Currency("GBP", "British Pound", "🇬🇧"),
    Currency("AED", "UAE Dirham", "🇦🇪"),
    Currency("AUD", "Australian Dollar", "🇦🇺"),
    Currency("CAD", "Canadian Dollar", "🇨🇦"),
    Currency("SGD", "Singapore Dollar", "🇸🇬"),
    Currency("JPY", "Japanese Yen", "🇯🇵"),
    Currency("CNY", "Chinese Yuan", "🇨🇳"),
    Currency("SAR", "Saudi Riyal", "🇸🇦"),
    Currency("KRW", "South Korean Won", "🇰🇷"),
    Currency("RUB", "Russian Ruble", "🇷🇺"),
    Currency("BRL", "Brazilian Real", "🇧🇷"),
    Currency("ZAR", "South African Rand", "🇿🇦")
)

val FALLBACK_RATES: Map<String, Double> = mapOf(
    "USD" to 1.0,
    "INR" to 83.25,
    "EUR" to 0.93,
    "GBP" to 0.79,
    "AED" to 3.67,
    "AUD" to 1.54,
    "CAD" to 1.37,
    "SGD" to 1.35,
    "JPY" to 155.20,
    "CNY" to 7.24,
    "SAR" to 3.75,
    "KRW" to 1378.0,
    "RUB" to 92.5,
    "BRL" to 5.15,
    "ZAR" to 18.6
)
