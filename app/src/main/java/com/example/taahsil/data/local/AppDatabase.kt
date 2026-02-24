package com.example.taahsil.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taahsil.data.local.dao.OrderDao
import com.example.taahsil.data.local.dao.PaymentDao
import com.example.taahsil.data.local.dao.ProductDao
import com.example.taahsil.data.local.dao.ShipmentDao
import com.example.taahsil.data.local.dao.UserDao
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.OrderProductCrossRef
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderProductCrossRef::class,
        ShipmentEntity::class,
        PaymentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val productDao: ProductDao
    abstract val orderDao: OrderDao
    abstract val shipmentDao: ShipmentDao
    abstract val paymentDao: PaymentDao

    companion object {
        const val DATABASE_NAME = "taahsile_db"
    }
}
