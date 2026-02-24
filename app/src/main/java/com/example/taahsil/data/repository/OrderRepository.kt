package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.OrderDao
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.OrderProductCrossRef
import com.example.taahsil.data.remote.OrderItemRequest
import com.example.taahsil.data.remote.OrderRequest
import com.example.taahsil.data.remote.TaahsilApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val api: TaahsilApi,
    private val orderDao: OrderDao
) {
    fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()

    fun getOrdersByCustomer(customerId: String): Flow<List<OrderEntity>> =
        orderDao.getOrdersByCustomer(customerId)

    suspend fun placeOrder(customerId: String, items: List<OrderItemRequest>): Result<OrderEntity> {
        return try {
            val order = api.createOrder(OrderRequest(customerId, items))
            orderDao.insertOrder(order)
            orderDao.insertOrderProducts(
                items.map { OrderProductCrossRef(order.orderId, it.productId, it.quantity) }
            )
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
