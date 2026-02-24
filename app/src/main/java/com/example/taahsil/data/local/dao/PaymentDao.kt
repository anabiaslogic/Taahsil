package com.example.taahsil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taahsil.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE paymentStatus = 'Pending'")
    fun getPendingPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    fun getPaymentForOrder(orderId: String): Flow<PaymentEntity?>
}
