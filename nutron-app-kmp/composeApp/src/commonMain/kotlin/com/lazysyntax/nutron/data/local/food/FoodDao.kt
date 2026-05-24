package com.lazysyntax.nutron.data.local.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lazysyntax.nutron.domain.models.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Update
    suspend fun updateFood(food: FoodEntity)


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

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :name || '%'")
    suspend fun searchFoodByName(name: String): List<FoodEntity>

    @Transaction
    @Query("SELECT * FROM foods WHERE  userId = :userId AND  isSynced = 0")
    fun getUnsyncedFoods(userId: String): Flow<List<FoodEntity>>
    @Query("SELECT COUNT(*) FROM foods WHERE userId = :userId AND  isSynced = 0")
    fun getUnsyncedFoodsCount(userId: String): Flow<Int>

    @Query("DELETE FROM foods")
    suspend fun clearAll()
}
