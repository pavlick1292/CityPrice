package com.example.cityprice.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityprice.data.DemoData
import com.example.cityprice.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val query: String = "",
    val city: String = "Партизанск",
    val results: List<Product> = emptyList(),
    val isSearching: Boolean = false,     // true => показываем Shimmer
    val cartProductIds: Set<Int> = emptySet() // какие уже добавили в корзину
)

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _cartIds = MutableStateFlow<Set<Int>>(emptySet())

    // Демо-«база»: на Шаге 3 заменим на Room
    private val allProducts: List<Product> = DemoData.products

    val uiState: StateFlow<HomeUiState> = combine(
        _query.debounce(300),   // не дёргаем поиск на каждую букву
        _cartIds
    ) { query, cartIds ->
        if (query.isBlank()) {
            HomeUiState(
                query = query,
                city = "Партизанск",
                results = emptyList(),
                isSearching = true, // пусто -> показываем shimmer
                cartProductIds = cartIds
            )
        } else {
            val filtered = allProducts.filter {
                it.name.contains(query, ignoreCase = true)
            }
            HomeUiState(
                query = query,
                city = "Партизанск",
                results = filtered,
                isSearching = false,
                cartProductIds = cartIds
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _cartIds.update { it + product.id }
        }
    }
}