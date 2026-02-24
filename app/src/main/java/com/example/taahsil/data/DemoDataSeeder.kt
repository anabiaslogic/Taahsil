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

        // --- Products (India's top export commodities with realistic INR prices) ---
        val products = listOf(
            ProductEntity("P001", "Organic Basmati Rice", "Food Grains", "1006.30", 58.0, 25.0),
            ProductEntity("P002", "Cold Pressed Coconut Oil", "Edible Oils", "1513.19", 385.0, 5.0),
            ProductEntity("P003", "Hand-Woven Silk Saree", "Textiles", "5007.20", 8500.0, 0.5),
            ProductEntity("P004", "Darjeeling Green Tea (Premium)", "Beverages", "0902.10", 2400.0, 1.0),
            ProductEntity("P005", "Turmeric Powder (Organic)", "Spices", "0910.30", 220.0, 10.0),
            ProductEntity("P006", "Cashew Nuts (W-240)", "Dry Fruits", "0801.31", 980.0, 10.0),
            ProductEntity("P007", "Granite Floor Tiles (Black Galaxy)", "Building Material", "6802.93", 320.0, 30.0),
            ProductEntity("P008", "Leather Wallet – Artisan Craft", "Leather Goods", "4202.31", 1450.0, 0.2),
            ProductEntity("P009", "Ayurvedic Hair Oil (Brahmi)", "Cosmetics", "3305.90", 480.0, 0.3),
            ProductEntity("P010", "Stainless Steel Utensils Set (6 pcs)", "Kitchenware", "7323.93", 2200.0, 3.0),
            ProductEntity("P011", "Cotton Bedsheet (King) 400TC", "Textiles", "6302.21", 1850.0, 1.5),
            ProductEntity("P012", "Alphonso Mango Pulp (Canned)", "Canned Food", "2008.99", 340.0, 0.85),
            ProductEntity("P013", "Rose Attar (Pure Essential Oil)", "Cosmetics", "3301.29", 12000.0, 0.1),
            ProductEntity("P014", "Handmade Brass Figurine", "Handicrafts", "8306.29", 3200.0, 1.2),
            ProductEntity("P015", "Jute Shopping Bags (Pack of 10)", "Packaging", "6305.10", 450.0, 2.0),
            ProductEntity("P016", "Neem Wood Comb Set", "Personal Care", "9615.11", 180.0, 0.05),
            ProductEntity("P017", "Rajasthani Block Print Fabric (5m)", "Textiles", "5208.31", 1200.0, 0.8)
        )
        db.productDao.insertProducts(products)

        val Day = 86400000L

        // --- Orders (realistic Indian B2B amounts in INR) ---
        val orders = listOf(
            OrderEntity("ORD-2024-001", "CUST-Mehta Exports", System.currentTimeMillis() - Day * 30, "Completed",  870000.0),
            OrderEntity("ORD-2024-002", "CUST-Al Falah Trading", System.currentTimeMillis() - Day * 20, "Completed", 510000.0),
            OrderEntity("ORD-2024-003", "CUST-Euro Spices GmbH", System.currentTimeMillis() - Day * 15, "Processing", 188000.0),
            OrderEntity("ORD-2024-004", "CUST-Singapore Silk Co", System.currentTimeMillis() - Day * 10, "Processing", 425000.0),
            OrderEntity("ORD-2024-005", "CUST-Mehta Exports", System.currentTimeMillis() - Day * 8,  "Completed",  315000.0),
            OrderEntity("ORD-2024-006", "CUST-Nairobi Grocers Ltd", System.currentTimeMillis() - Day * 5, "Pending",   96000.0),
            OrderEntity("ORD-2024-007", "CUST-UK Heritage Crafts", System.currentTimeMillis() - Day * 3, "Processing", 672000.0),
            OrderEntity("ORD-2024-008", "CUST-Japan Natural Foods", System.currentTimeMillis() - Day * 1, "Pending",  230000.0)
        )
        orders.forEach { db.orderDao.insertOrder(it) }

        // Order-Product cross refs
        val crossRefs = listOf(
            OrderProductCrossRef("ORD-2024-001", "P001", 5000),
            OrderProductCrossRef("ORD-2024-001", "P005", 600),
            OrderProductCrossRef("ORD-2024-002", "P006", 300),
            OrderProductCrossRef("ORD-2024-002", "P012", 200),
            OrderProductCrossRef("ORD-2024-003", "P004", 50),
            OrderProductCrossRef("ORD-2024-003", "P009", 120),
            OrderProductCrossRef("ORD-2024-004", "P003", 40),
            OrderProductCrossRef("ORD-2024-004", "P011", 80),
            OrderProductCrossRef("ORD-2024-005", "P007", 500),
            OrderProductCrossRef("ORD-2024-006", "P001", 1000),
            OrderProductCrossRef("ORD-2024-007", "P008", 200),
            OrderProductCrossRef("ORD-2024-007", "P014", 100),
            OrderProductCrossRef("ORD-2024-008", "P002", 300)
        )
        db.orderDao.insertOrderProducts(crossRefs)

        // --- Shipments ---
        val shipments = listOf(
            ShipmentEntity("SHP-001", "ORD-2024-001", "MAEU123456789", "Mumbai Port (INNSA)", "Rotterdam (NLRTM)", "Bill of Lading", "Delivered"),
            ShipmentEntity("SHP-002", "ORD-2024-002", "COSCO987654321", "Chennai Port (INMAA)", "Jebel Ali (AEJEA)", "Sea Waybill", "Delivered"),
            ShipmentEntity("SHP-003", "ORD-2024-003", "EVER112233445", "Nhava Sheva (INNSA)", "Hamburg (DEHAM)", "Bill of Lading", "In Transit"),
            ShipmentEntity("SHP-004", "ORD-2024-004", "MSC998877665", "Kolkata Port (INCCU)", "Singapore (SGSIN)", "Sea Waybill", "Customs"),
            ShipmentEntity("SHP-005", "ORD-2024-005", "OOCL554433221", "Kandla Port (INIKP)", "Mombasa (KEMBA)", "Bill of Lading", "Delivered"),
            ShipmentEntity("SHP-006", "ORD-2024-007", "HASL778899001", "Mumbai Port (INNSA)", "Felixstowe (GBFXT)", "Bill of Lading", "Booked"),
            ShipmentEntity("SHP-007", "ORD-2024-008", "YANG443322110", "Chennai Port (INMAA)", "Osaka (JPOSA)", "Sea Waybill", "Booked")
        )
        shipments.forEach { db.shipmentDao.insertShipment(it) }

        // --- Payments ---
        val payments = listOf(
            PaymentEntity("PAY-001", "ORD-2024-001", "Wire Transfer (SWIFT)", 870000.0, "Paid"),
            PaymentEntity("PAY-002", "ORD-2024-002", "Letter of Credit", 510000.0, "Paid"),
            PaymentEntity("PAY-003", "ORD-2024-003", "Advance Payment (30%)", 56400.0, "Paid"),
            PaymentEntity("PAY-004", "ORD-2024-003", "Balance on Delivery (70%)", 131600.0, "Pending"),
            PaymentEntity("PAY-005", "ORD-2024-004", "Letter of Credit", 425000.0, "Pending"),
            PaymentEntity("PAY-006", "ORD-2024-005", "Bank Transfer (NEFT)", 315000.0, "Paid"),
            PaymentEntity("PAY-007", "ORD-2024-006", "UPI Business", 96000.0, "Pending"),
            PaymentEntity("PAY-008", "ORD-2024-007", "Wire Transfer (SWIFT)", 672000.0, "Pending"),
            PaymentEntity("PAY-009", "ORD-2024-008", "Documentary Collection", 230000.0, "Pending")
        )
        payments.forEach { db.paymentDao.insertPayment(it) }
    }
}
