package com.example.taahsil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val name: String,
    val email: String,
    val role: String, // Admin, Staff, Customer
    val companyName: String,
    val contactPerson: String,
    val country: String
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val productId: String,
    val productName: String,
    val category: String,
    val hsCode: String,
    val unitPrice: Double,
    val weight: Double
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val customerId: String,
    val orderDate: Long,
    val status: String,
    val totalAmount: Double
)

@Entity(tableName = "order_product_cross_ref", primaryKeys = ["orderId", "productId"])
data class OrderProductCrossRef(
    val orderId: String,
    val productId: String,
    val quantity: Int
)

@Entity(tableName = "shipments")
data class ShipmentEntity(
    @PrimaryKey
    val shipmentId: String,
    val orderId: String,
    val shipmentTracking: String,
    val originPort: String,
    val destinationPort: String,
    val billType: String,
    val shipmentStatus: String
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey
    val paymentId: String,
    val orderId: String,
    val paymentMethod: String, // Bank Transfer, Letter of Credit
    val amount: Double,
    val paymentStatus: String // Pending, Paid
)
