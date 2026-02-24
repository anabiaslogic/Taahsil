package com.example.taahsil.di

import android.app.Application
import androidx.room.Room
import com.example.taahsil.data.local.AppDatabase
import com.example.taahsil.data.local.dao.OrderDao
import com.example.taahsil.data.local.dao.PaymentDao
import com.example.taahsil.data.local.dao.ProductDao
import com.example.taahsil.data.local.dao.ShipmentDao
import com.example.taahsil.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao
    @Provides fun provideProductDao(db: AppDatabase): ProductDao = db.productDao
    @Provides fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao
    @Provides fun provideShipmentDao(db: AppDatabase): ShipmentDao = db.shipmentDao
    @Provides fun providePaymentDao(db: AppDatabase): PaymentDao = db.paymentDao
}
