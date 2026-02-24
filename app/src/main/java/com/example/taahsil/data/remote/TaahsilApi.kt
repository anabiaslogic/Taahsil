package com.example.taahsil.data.remote

import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.data.local.entity.UserEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaahsilApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body user: UserEntity): AuthResponse

    @GET("products")
    suspend fun getProducts(): List<ProductEntity>

    @GET("products/search")
    suspend fun searchProducts(@Query("query") query: String): List<ProductEntity>

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): OrderEntity

    @GET("orders")
    suspend fun getCustomerOrders(@Query("customerId") customerId: String): List<OrderEntity>

    @GET("shipments/{orderId}")
    suspend fun getShipmentInfo(@Path("orderId") orderId: String): ShipmentEntity

    @GET("payments/pending")
    suspend fun getPendingPayments(): List<PaymentEntity>
}

data class LoginRequest(
    val email: String,
    val passwordHash: String
)

data class AuthResponse(
    val token: String,
    val user: UserEntity
)

data class OrderRequest(
    val customerId: String,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val productId: String,
    val quantity: Int
)
