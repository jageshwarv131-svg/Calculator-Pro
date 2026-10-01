package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CalculatorMode {
    STANDARD,
    SCIENTIFIC
}

@Entity(tableName = "calc_history")
data class CalcHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mode: String, // "STANDARD" or "SCIENTIFIC"
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
