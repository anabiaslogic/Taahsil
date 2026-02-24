package com.example.taahsil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taahsil.data.local.entity.ShipmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipment(shipment: ShipmentEntity)

    @Query("SELECT * FROM shipments WHERE orderId = :orderId")
    fun getShipmentForOrder(orderId: String): Flow<ShipmentEntity?>

    @Query("SELECT * FROM shipments")
    fun getActiveShipments(): Flow<List<ShipmentEntity>>
}
