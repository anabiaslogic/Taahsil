package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.ProductDao
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.remote.TaahsilApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val api: TaahsilApi,
    private val productDao: ProductDao
) {
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    suspend fun refreshProducts() {
        try {
            val products = api.getProducts()
            productDao.insertProducts(products)
        } catch (_: Exception) { /* offline mode - use cached */ }
    }
}
