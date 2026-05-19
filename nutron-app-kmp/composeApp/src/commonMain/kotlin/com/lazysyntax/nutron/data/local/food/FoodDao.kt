package com.lazysyntax.nutron.data.local.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE barcode = :barcode")
    suspend fun getFoodByCode(barcode: String): FoodEntity?

    @Query("DELETE FROM foods WHERE barcode = :barcode")
    suspend fun deleteFoodByCode(barcode: String)

    @Query("SELECT * FROM foods WHERE barcode = :barcode")
    suspend fun getFoodByBarcode(barcode: String?): FoodEntity?

    @Query("DELETE FROM foods WHERE barcode = :barcode")
    suspend fun deleteFoodByBarcode(barcode: String?)

}
