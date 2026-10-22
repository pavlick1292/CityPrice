package com.example.cityprice.domain.model

data class Product(
    val id: Int,
    val name: String,
    val unit: String, // "1 л", "500 г" и т.п.
    val prices: Map<String, Int> // магазин -> цена в рублях
)