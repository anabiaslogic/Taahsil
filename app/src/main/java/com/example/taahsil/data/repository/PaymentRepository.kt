package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.PaymentDao
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.remote.TaahsilApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val api: TaahsilApi,
    private val paymentDao: PaymentDao
) {
    fun getPendingPayments(): Flow<List<PaymentEntity>> = paymentDao.getPendingPayments()

    fun getPaymentForOrder(orderId: String): Flow<PaymentEntity?> =
        paymentDao.getPaymentForOrder(orderId)

    suspend fun refreshPendingPayments() {
        try {
            val payments = api.getPendingPayments()
            payments.forEach { paymentDao.insertPayment(it) }
        } catch (_: Exception) { /* offline mode */ }
    }
}
