package com.example.taahsil.data

import com.example.taahsil.data.local.AppDatabase
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.OrderProductCrossRef
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.local.entity.ShipmentEntity
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDataSeeder @Inject constructor(
    private val db: AppDatabase
) {
    suspend fun seedIfEmpty() {
        val existing = db.productDao.getAllProducts().firstOrNull()
        if (!existing.isNullOrEmpty()) return // Already seeded

        // --- Products ---
        val products = listOf(
            ProductEntity("P001", "Organic Basmati Rice", "Food Grains", "1006.30", 850.0, 25.0),
            ProductEntity("P002", "Cold Pressed Coconut Oil", "Edible Oils", "1513.19", 420.0, 5.0),
            ProductEntity("P003", "Hand-Woven Silk Saree", "Textiles", "5007.20", 2500.0, 0.5),
            ProductEntity("P004", "Darjeeling Green Tea", "Beverages", "0902.10", 1200.0, 1.0),
            ProductEntity("P005", "Turmeric Powder (Organic)", "Spices", "0910.30", 380.0, 10.0),
            ProductEntity("P006", "Cashew Nuts (W-240)", "Dry Fruits", "0801.31", 960.0, 10.0),
            ProductEntity("P007", "Granite Floor Tiles", "Building Material", "6802.93", 45.0, 30.0),
            ProductEntity("P008", "Leather Wallet (Men)", "Leather Goods", "4202.31", 22.0, 0.2),
            ProductEntity("P009", "Ayurvedic Hair Oil", "Cosmetics", "3305.90", 8.50, 0.3),
            ProductEntity("P010", "Stainless Steel Utensils Set", "Kitchenware", "7323.93", 35.0, 3.0),
            ProductEntity("P011", "Cotton Bedsheet (King)", "Textiles", "6302.21", 18.0, 1.5),
            ProductEntity("P012", "Alphonso Mango Pulp", "Canned Food", "2008.99", 5.50, 0.85)
        )
        db.productDao.insertProducts(products)

        // --- Orders ---
        val orders = listOf(
            OrderEntity("ORD-2024-001", "CUST-001", System.currentTimeMillis() - 86400000 * 5, "Completed", 42500.0),
            OrderEntity("ORD-2024-002", "CUST-002", System.currentTimeMillis() - 86400000 * 3, "Processing", 18200.0),
            OrderEntity("ORD-2024-003", "CUST-001", System.currentTimeMillis() - 86400000 * 1, "Pending", 9600.0),
            OrderEntity("ORD-2024-004", "CUST-003", System.currentTimeMillis(), "Processing", 31000.0),
            OrderEntity("ORD-2024-005", "CUST-002", System.currentTimeMillis() - 86400000 * 10, "Completed", 15750.0)
        )
        orders.forEach { db.orderDao.insertOrder(it) }

        // Order-Product cross refs
        val crossRefs = listOf(
            OrderProductCrossRef("ORD-2024-001", "P001", 50),
            OrderProductCrossRef("ORD-2024-001", "P005", 20),
            OrderProductCrossRef("ORD-2024-002", "P003", 5),
            OrderProductCrossRef("ORD-2024-002", "P006", 10),
            OrderProductCrossRef("ORD-2024-003", "P004", 8),
            OrderProductCrossRef("ORD-2024-004", "P002", 40),
            OrderProductCrossRef("ORD-2024-004", "P007", 100),
            OrderProductCrossRef("ORD-2024-005", "P008", 150)
        )
        db.orderDao.insertOrderProducts(crossRefs)

        // --- Shipments ---
        val shipments = listOf(
            ShipmentEntity("SHP-001", "ORD-2024-001", "MAEU123456789", "Mumbai Port (INNSA)", "Rotterdam (NLRTM)", "Bill of Lading", "Delivered"),
            ShipmentEntity("SHP-002", "ORD-2024-002", "COSCO987654321", "Chennai Port (INMAA)", "Dubai (AEJEA)", "Sea Waybill", "In Transit"),
            ShipmentEntity("SHP-003", "ORD-2024-004", "EVER112233445", "Nhava Sheva (INNSA)", "Hamburg (DEHAM)", "Bill of Lading", "Customs"),
            ShipmentEntity("SHP-004", "ORD-2024-005", "MSC998877665", "Kolkata Port (INCCU)", "Singapore (SGSIN)", "Sea Waybill", "Booked")
        )
        shipments.forEach { db.shipmentDao.insertShipment(it) }

        // --- Payments ---
        val payments = listOf(
            PaymentEntity("PAY-001", "ORD-2024-001", "Wire Transfer", 42500.0, "Paid"),
            PaymentEntity("PAY-002", "ORD-2024-002", "Letter of Credit", 18200.0, "Pending"),
            PaymentEntity("PAY-003", "ORD-2024-003", "UPI", 9600.0, "Pending"),
            PaymentEntity("PAY-004", "ORD-2024-004", "Wire Transfer", 15500.0, "Pending"),
            PaymentEntity("PAY-005", "ORD-2024-005", "Bank Transfer", 15750.0, "Paid")
        )
        payments.forEach { db.paymentDao.insertPayment(it) }
    }
}
