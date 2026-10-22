package com.example.cityprice.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityprice.data.DemoData
import com.example.cityprice.data.repository.ProductRepository
import com.example.cityprice.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val items: List<Product> = emptyList(),
    val totalsByStore: Map<String, Int> = emptyMap(), // магазин -> итог по корзине
    val cheapestStore: String? = null,
    val mostExpensiveStore: String? = null,
    val savings: Int = 0, // разница между дорогим и дешёвым
    val isEmpty: Boolean = true
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = combine(
        repository.observeProducts(),
        repository.observeCartIds()
    ) { allProducts, cartIds ->
        val inCart = allProducts.filter { it.id in cartIds }

        // Считаем сумму по каждому магазину (используем полный список магазинов)
        val totals = DemoData.stores.associateWith { store ->
            inCart.sumOf { product -> product.prices[store] ?: 0 }
        }

        // Учитываем только магазины, где есть хотя бы один товар
        val nonEmptyTotals = totals.filterValues { it > 0 }

        val cheapest = nonEmptyTotals.minByOrNull { it.value }
        val expensive = nonEmptyTotals.maxByOrNull { it.value }
        val savings = if (cheapest != null && expensive != null) {
            expensive.value - cheapest.value
        } else 0

        CartUiState(
            items = inCart,
            totalsByStore = totals,
            cheapestStore = cheapest?.key,
            mostExpensiveStore = expensive?.key,
            savings = savings,
            isEmpty = inCart.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CartUiState()
    )

    fun removeFromCart(product: Product) {
        viewModelScope.launch { repository.removeFromCart(product.id) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() }
    }
}