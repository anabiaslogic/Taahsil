package com.example.taahsil.ui.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.PaymentEntity
import com.example.taahsil.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentState(
    val pendingPayments: List<PaymentEntity> = emptyList(),
    val totalPending: Double = 0.0,
    val totalPaid: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    init {
        loadPayments()
    }

    private fun loadPayments() {
        paymentRepository.getPendingPayments().onEach { payments ->
            val pending = payments.filter { it.paymentStatus == "Pending" }
            val paid = payments.filter { it.paymentStatus == "Paid" }
            _state.value = _state.value.copy(
                pendingPayments = payments,
                totalPending = pending.sumOf { it.amount },
                totalPaid = paid.sumOf { it.amount },
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            paymentRepository.refreshPendingPayments()
        }
    }
}
