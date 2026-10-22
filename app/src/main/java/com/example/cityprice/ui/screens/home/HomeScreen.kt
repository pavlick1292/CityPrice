package com.example.cityprice.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityprice.domain.model.Product
import com.example.cityprice.ui.components.PriceRow
import com.example.cityprice.ui.components.ShimmerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showCityDialog by remember { mutableStateOf(false) }
    var tempCity by remember { mutableStateOf(state.city) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            "CityPrice",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))

        // Поле поиска
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Найти товар…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // Город
        TextButton(onClick = { showCityDialog = true }) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text("Город: ${state.city} (изменить)")
        }

        Spacer(Modifier.height(8.dp))

        if (state.isSearching) {
            // Пустой поиск: призрачная карточка + подсказка
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShimmerCard(modifier = Modifier.padding(horizontal = 8.dp))
                Text(
                    "Введите товар, чтобы увидеть цены",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else if (state.results.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    "Ничего не найдено по запросу «${state.query}»",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.results, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        alreadyInCart = product.id in state.cartProductIds,
                        onAdd = { viewModel.addToCart(product) }
                    )
                }
            }
        }
    }

    // Диалог выбора города (пока только Партизанск, но с полем ввода под будущее)
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Город") },
            text = {
                OutlinedTextField(
                    value = tempCity,
                    onValueChange = { tempCity = it },
                    label = { Text("Название города") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    // На MVP фиксируем Партизанск
                    tempCity = "Партизанск"
                    showCityDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("Отмена") }
            }
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    alreadyInCart: Boolean,
    onAdd: () -> Unit
) {
    val bestPrice = product.prices.values.minOrNull()
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        product.unit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            product.prices.forEach { (store, price) ->
                PriceRow(store = store, price = price, isBest = price == bestPrice)
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    onClick = onAdd,
                    enabled = !alreadyInCart,
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(if (alreadyInCart) "В корзине" else "В корзину")
                }
            }
        }
    }
}