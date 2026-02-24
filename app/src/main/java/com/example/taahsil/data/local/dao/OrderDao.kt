package com.example.taahsil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.OrderProductCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProducts(crossRefs: List<OrderProductCrossRef>)

    @Transaction
    @Query("SELECT * FROM orders WHERE customerId = :customerId")
    fun getOrdersByCustomer(customerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderEntity>>
}
