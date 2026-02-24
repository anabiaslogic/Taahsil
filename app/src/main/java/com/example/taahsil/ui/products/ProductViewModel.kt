package com.example.taahsil.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.local.entity.ProductEntity
import com.example.taahsil.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductState(
    val products: List<ProductEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        productRepository.getAllProducts().onEach { products ->
            _state.value = _state.value.copy(products = products, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        if (query.isBlank()) {
            loadProducts()
        } else {
            productRepository.searchProducts(query).onEach { products ->
                _state.value = _state.value.copy(products = products)
            }.launchIn(viewModelScope)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            productRepository.refreshProducts()
        }
    }
}
