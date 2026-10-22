package com.example.cityprice

import app.cash.turbine.test
import com.example.cityprice.data.repository.ProductRepository
import com.example.cityprice.domain.model.Product
import com.example.cityprice.ui.screens.cart.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    // Фейковый репозиторий для теста
    private class FakeRepository(
        private val products: List<Product>,
        private val cartIds: MutableStateFlow<Set<Int>>
    ) : ProductRepository {
        override fun observeProducts() = flowOf(products)
        override fun observeCartIds() = cartIds
        override suspend fun addToCart(productId: Int) { cartIds.value = cartIds.value + productId }
        override suspend fun removeFromCart(productId: Int) { cartIds.value = cartIds.value - productId }
        override suspend fun clearCart() { cartIds.value = emptySet() }
        override suspend fun seedIfEmpty() {}
    }

    private val mockProducts = listOf(
        Product(1, "Хлеб", "400г", mapOf("Реми" to 45, "Пятёрочка" to 39, "Синторг" to 42)),
        Product(2, "Молоко", "1л", mapOf("Реми" to 89, "Пятёрочка" to 84, "Синторг" to 95))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calculate cheapest store correctly`() = runTest {
        val cartIds = MutableStateFlow(setOf(1, 2))
        val repo = FakeRepository(mockProducts, cartIds)
        val viewModel = CartViewModel(repo)

        viewModel.uiState.test {
            // Пропускаем initialValue (empty)
            awaitItem()

            val state = awaitItem()

            // Итог: Реми = 45+89=134, Пятёрочка = 39+84=123, Синторг = 42+95=137
            assertEquals(134, state.totalsByStore["Реми"])
            assertEquals(123, state.totalsByStore["Пятёрочка"])
            assertEquals(137, state.totalsByStore["Синторг"])

            // Самая дешевая — Пятёрочка
            assertEquals("Пятёрочка", state.cheapestStore)
            // Экономия = 137 (Синторг) - 123 (Пятёрочка) = 14
            assertEquals(14, state.savings)

            cancelAndIgnoreRemainingEvents()
        }
    }
}