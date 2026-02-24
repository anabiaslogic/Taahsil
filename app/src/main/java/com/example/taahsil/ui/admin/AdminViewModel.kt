package com.example.taahsil.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.data.local.entity.UserEntity
import com.example.taahsil.data.repository.AuthRepository
import com.example.taahsil.data.repository.OrderRepository
import com.example.taahsil.data.repository.PaymentRepository
import com.example.taahsil.data.repository.ProductRepository
import com.example.taahsil.data.repository.ShipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class AdminState(
    val users: List<UserEntity> = emptyList(),
    val totalProducts: Int = 0,
    val totalOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val activeShipments: Int = 0,
    val pendingPayments: Int = 0,
    val paidAmount: Double = 0.0,
    val pendingAmount: Double = 0.0,
    val recentOrders: List<OrderEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state.asStateFlow()

    init {
        loadAdminData()
    }

    private fun loadAdminData() {
        _state.value = _state.value.copy(isLoading = true)

        authRepository.getAllUsers().onEach { users ->
            _state.value = _state.value.copy(users = users)
        }.launchIn(viewModelScope)

        productRepository.getAllProducts().onEach { products ->
            _state.value = _state.value.copy(totalProducts = products.size)
        }.launchIn(viewModelScope)

        orderRepository.getAllOrders().onEach { orders ->
            _state.value = _state.value.copy(
                totalOrders = orders.size,
                totalRevenue = orders.sumOf { it.totalAmount },
                recentOrders = orders.take(5)
            )
        }.launchIn(viewModelScope)

        shipmentRepository.getActiveShipments().onEach { shipments ->
            _state.value = _state.value.copy(activeShipments = shipments.size)
        }.launchIn(viewModelScope)

        paymentRepository.getPendingPayments().onEach { payments ->
            _state.value = _state.value.copy(
                pendingPayments = payments.filter { it.paymentStatus == "Pending" }.size,
                paidAmount = payments.filter { it.paymentStatus == "Paid" }.sumOf { it.amount },
                pendingAmount = payments.filter { it.paymentStatus == "Pending" }.sumOf { it.amount },
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }
}
