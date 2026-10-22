package com.example.cityprice.data.repository

import com.example.cityprice.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    fun observeCartIds(): Flow<Set<Int>>
    suspend fun addToCart(productId: Int)
    suspend fun removeFromCart(productId: Int)
    suspend fun clearCart()
    suspend fun seedIfEmpty()
}
