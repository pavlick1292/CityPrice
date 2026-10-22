package com.example.cityprice.data

import com.example.cityprice.domain.model.Product

/**
 * Временные демо-данные. На Шаге 3 переедем в Room
 * (с сохранением этих же товаров при первом запуске).
 * Магазины: Реми, Пятёрочка, Синторг (Партизанск).
 */
object DemoData {

    const val STORE_REMI = "Реми"
    const val STORE_PYATEROCHKA = "Пятёрочка"
    const val STORE_SINTORG = "Синторг"

    val stores = listOf(STORE_REMI, STORE_PYATEROCHKA, STORE_SINTORG)

    val products: List<Product> = listOf(
        Product(1, "Молоко 2.5%", "1 л", mapOf(STORE_REMI to 89, STORE_PYATEROCHKA to 84, STORE_SINTORG to 95)),
        Product(2, "Хлеб белый", "400 г", mapOf(STORE_REMI to 45, STORE_PYATEROCHKA to 39, STORE_SINTORG to 42)),
        Product(3, "Сахар-песок", "1 кг", mapOf(STORE_REMI to 79, STORE_PYATEROCHKA to 72, STORE_SINTORG to 85)),
        Product(4, "Яйца С1", "10 шт", mapOf(STORE_REMI to 129, STORE_PYATEROCHKA to 119, STORE_SINTORG to 135)),
        Product(5, "Масло сливочное", "180 г", mapOf(STORE_REMI to 189, STORE_PYATEROCHKA to 179, STORE_SINTORG to 199)),
        Product(6, "Сыр Российский", "200 г", mapOf(STORE_REMI to 249, STORE_PYATEROCHKA to 229, STORE_SINTORG to 259)),
        Product(7, "Куриное филе", "1 кг", mapOf(STORE_REMI to 389, STORE_PYATEROCHKA to 369, STORE_SINTORG to 399)),
        Product(8, "Рис круглозёрный", "900 г", mapOf(STORE_REMI to 119, STORE_PYATEROCHKA to 109, STORE_SINTORG to 129)),
        Product(9, "Макароны спагетти", "450 г", mapOf(STORE_REMI to 89, STORE_PYATEROCHKA to 79, STORE_SINTORG to 95)),
        Product(10, "Картофель", "1 кг", mapOf(STORE_REMI to 59, STORE_PYATEROCHKA to 49, STORE_SINTORG to 55)),
        Product(11, "Морковь", "1 кг", mapOf(STORE_REMI to 49, STORE_PYATEROCHKA to 45, STORE_SINTORG to 52)),
        Product(12, "Лук репчатый", "1 кг", mapOf(STORE_REMI to 45, STORE_PYATEROCHKA to 39, STORE_SINTORG to 48)),
        Product(13, "Чай чёрный", "100 г", mapOf(STORE_REMI to 159, STORE_PYATEROCHKA to 149, STORE_SINTORG to 169)),
        Product(14, "Кофе молотый", "250 г", mapOf(STORE_REMI to 429, STORE_PYATEROCHKA to 399, STORE_SINTORG to 449)),
        Product(15, "Сок апельсиновый", "1 л", mapOf(STORE_REMI to 129, STORE_PYATEROCHKA to 119, STORE_SINTORG to 139)),
        Product(16, "Кефир 1%", "1 л", mapOf(STORE_REMI to 79, STORE_PYATEROCHKA to 69, STORE_SINTORG to 85)),
        Product(17, "Сметана 15%", "300 г", mapOf(STORE_REMI to 119, STORE_PYATEROCHKA to 109, STORE_SINTORG to 129)),
        Product(18, "Творог 5%", "200 г", mapOf(STORE_REMI to 139, STORE_PYATEROCHKA to 125, STORE_SINTORG to 149)),
        Product(19, "Вода питьевая", "5 л", mapOf(STORE_REMI to 89, STORE_PYATEROCHKA to 79, STORE_SINTORG to 95)),
        Product(20, "Соль поваренная", "1 кг", mapOf(STORE_REMI to 29, STORE_PYATEROCHKA to 25, STORE_SINTORG to 35)),
        Product(21, "Мука пшеничная", "2 кг", mapOf(STORE_REMI to 129, STORE_PYATEROCHKA to 119, STORE_SINTORG to 139)),
        Product(22, "Гречка", "800 г", mapOf(STORE_REMI to 99, STORE_PYATEROCHKA to 89, STORE_SINTORG to 109)),
        Product(23, "Сосиски молочные", "400 г", mapOf(STORE_REMI to 219, STORE_PYATEROCHKA to 199, STORE_SINTORG to 229)),
        Product(24, "Печенье овсяное", "200 г", mapOf(STORE_REMI to 89, STORE_PYATEROCHKA to 75, STORE_SINTORG to 95)),
        Product(25, "Шоколад молочный", "90 г", mapOf(STORE_REMI to 99, STORE_PYATEROCHKA to 89, STORE_SINTORG to 105))
    )
}