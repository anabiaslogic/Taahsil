package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.ShipmentDao
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.data.remote.TaahsilApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShipmentRepository @Inject constructor(
    private val api: TaahsilApi,
    private val shipmentDao: ShipmentDao
) {
    fun getActiveShipments(): Flow<List<ShipmentEntity>> = shipmentDao.getActiveShipments()

    fun getShipmentForOrder(orderId: String): Flow<ShipmentEntity?> =
        shipmentDao.getShipmentForOrder(orderId)

    suspend fun refreshShipment(orderId: String) {
        try {
            val shipment = api.getShipmentInfo(orderId)
            shipmentDao.insertShipment(shipment)
        } catch (_: Exception) { /* offline mode */ }
    }
}
