package com.example.taahsil.ui.shipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.ShipmentEntity
import com.example.taahsil.data.repository.ShipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShipmentState(
    val shipments: List<ShipmentEntity> = emptyList(),
    val selectedShipment: ShipmentEntity? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ShipmentViewModel @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ShipmentState())
    val state: StateFlow<ShipmentState> = _state.asStateFlow()

    init {
        loadShipments()
    }

    private fun loadShipments() {
        shipmentRepository.getActiveShipments().onEach { shipments ->
            _state.value = _state.value.copy(shipments = shipments, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun selectShipment(shipment: ShipmentEntity) {
        _state.value = _state.value.copy(selectedShipment = shipment)
    }

    fun refreshShipment(orderId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            shipmentRepository.refreshShipment(orderId)
        }
    }
}
