package com.example.cityprice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cityprice.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val unit: String,
    val pricesRaw: String
) {
    fun toDomain(): Product = Product(
        id = id,
        name = name,
        unit = unit,
        prices = pricesRaw.split(";").associate { pair ->
            val (store, price) = pair.split(":")
            store to price.toInt()
        }
    )

    companion object {
        fun fromDomain(p: Product): ProductEntity = ProductEntity(
            id = p.id,
            name = p.name,
            unit = p.unit,
            pricesRaw = p.prices.entries.joinToString(";") { "${it.key}:${it.value}" }
        )
    }
}
