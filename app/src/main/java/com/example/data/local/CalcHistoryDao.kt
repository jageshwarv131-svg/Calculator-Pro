package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CalcHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CalcHistoryDao {
    @Query("SELECT * FROM calc_history WHERE mode = :mode ORDER BY id DESC")
    fun getHistoryByMode(mode: String): Flow<List<CalcHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: CalcHistoryItem)

    @Query("DELETE FROM calc_history WHERE mode = :mode")
    suspend fun clearHistoryByMode(mode: String)
}
