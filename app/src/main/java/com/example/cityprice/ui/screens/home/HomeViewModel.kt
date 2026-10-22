package com.example.cityprice.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityprice.data.repository.ProductRepository
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
    val isSearching: Boolean = false,
    val cartProductIds: Set<Int> = emptySet(),
    val loaded: Boolean = false
)

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        _query.debounce(300),
        repository.observeProducts(),
        repository.observeCartIds()
    ) { query, products, cartIds ->
        val filtered = if (query.isBlank()) emptyList()
        else products.filter { it.name.contains(query, ignoreCase = true) }

        HomeUiState(
            query = query,
            city = "Партизанск",
            results = filtered,
            isSearching = query.isBlank(),
            cartProductIds = cartIds,
            loaded = true
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun addToCart(product: Product) {
        viewModelScope.launch { repository.addToCart(product.id) }
    }
}