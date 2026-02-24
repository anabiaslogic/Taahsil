package com.example.taahsil.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.OrderEntity
import com.example.taahsil.data.remote.OrderItemRequest
import com.example.taahsil.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int = 1
)

data class OrderState(
    val orders: List<OrderEntity> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val customerId: String = "",
    val isLoading: Boolean = false,
    val orderPlaced: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        orderRepository.getAllOrders().onEach { orders ->
            _state.value = _state.value.copy(orders = orders, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun addToCart(item: CartItem) {
        val currentCart = _state.value.cart.toMutableList()
        val existingIndex = currentCart.indexOfFirst { it.productId == item.productId }
        if (existingIndex >= 0) {
            currentCart[existingIndex] = currentCart[existingIndex].copy(
                quantity = currentCart[existingIndex].quantity + item.quantity
            )
        } else {
            currentCart.add(item)
        }
        _state.value = _state.value.copy(cart = currentCart)
    }

    fun removeFromCart(productId: String) {
        _state.value = _state.value.copy(
            cart = _state.value.cart.filter { it.productId != productId }
        )
    }

    fun updateQuantity(productId: String, quantity: Int) {
        _state.value = _state.value.copy(
            cart = _state.value.cart.map {
                if (it.productId == productId) it.copy(quantity = quantity) else it
            }
        )
    }

    fun placeOrder(customerId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val items = _state.value.cart.map {
                OrderItemRequest(it.productId, it.quantity)
            }
            val result = orderRepository.placeOrder(customerId, items)
            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        orderPlaced = true,
                        cart = emptyList()
                    )
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Order placement failed"
                    )
                }
            )
        }
    }

    fun getCartTotal(): Double = _state.value.cart.sumOf { it.unitPrice * it.quantity }
}
