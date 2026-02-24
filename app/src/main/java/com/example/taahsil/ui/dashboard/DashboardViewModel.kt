package com.example.taahsil.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.local.entity.ShipmentEntity
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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardState(
    val activeShipments: List<ShipmentEntity> = emptyList(),
    val pendingPayments: List<PaymentEntity> = emptyList(),
    val recentOrders: List<OrderEntity> = emptyList(),
    val totalProducts: Int = 0,
    val isLoading: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val shipmentRepo: ShipmentRepository,
    private val paymentRepo: PaymentRepository,
    private val orderRepo: OrderRepository,
    private val productRepo: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        _state.value = _state.value.copy(isLoading = true)

        shipmentRepo.getActiveShipments().onEach { shipments ->
            _state.value = _state.value.copy(activeShipments = shipments)
        }.launchIn(viewModelScope)

        paymentRepo.getPendingPayments().onEach { payments ->
            _state.value = _state.value.copy(pendingPayments = payments)
        }.launchIn(viewModelScope)

        orderRepo.getAllOrders().onEach { orders ->
            _state.value = _state.value.copy(recentOrders = orders.take(5))
        }.launchIn(viewModelScope)

        productRepo.getAllProducts().onEach { products ->
            _state.value = _state.value.copy(totalProducts = products.size, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            productRepo.refreshProducts()
            paymentRepo.refreshPendingPayments()
        }
    }
}
