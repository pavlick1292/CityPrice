package com.example.cityprice.data.repository

import com.example.cityprice.data.DemoData
import com.example.cityprice.data.local.CartDao
import com.example.cityprice.data.local.CartItemEntity
import com.example.cityprice.data.local.ProductDao
import com.example.cityprice.data.local.ProductEntity
import com.example.cityprice.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> =
        productDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeCartIds(): Flow<Set<Int>> =
        cartDao.observeAll().map { list -> list.map { it.productId }.toSet() }

    override suspend fun addToCart(productId: Int) {
        cartDao.insert(CartItemEntity(productId))
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.remove(productId)
    }

    override suspend fun clearCart() {
        cartDao.clear()
    }

    override suspend fun seedIfEmpty() {
        if (productDao.count() == 0) {
            productDao.insertAll(DemoData.products.map { ProductEntity.fromDomain(it) })
        }
    }
}
