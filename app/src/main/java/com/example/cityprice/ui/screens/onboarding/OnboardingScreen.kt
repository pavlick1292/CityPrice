package com.example.cityprice.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityprice.ui.components.BigElevatedButton

private data class OnboardPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardPage(
        Icons.Filled.Search,
        "Введи товар",
        "Найди молоко, хлеб или любой другой товар — мы покажем цены во всех магазинах Партизанска."
    ),
    OnboardPage(
        Icons.Filled.ShoppingCart,
        "Добавь в корзину",
        "Формируй список покупок. Кнопка «+» на карточке товара добавит его в корзину сравнения."
    ),
    OnboardPage(
        Icons.Filled.CheckCircle,
        "Узнай, где выгоднее",
        "Мы посчитаем итоговую сумму по каждому магазину и подскажем, где весь чек дешевле."
    )
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "CityPrice",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Сравнивай цены — покупай выгодно",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            pages.forEach { page ->
                OnboardCard(page)
            }
        }

        BigElevatedButton(
            text = "Начать",
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OnboardCard(page: OnboardPage) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(page.title, style = MaterialTheme.typography.titleLarge)
                Text(page.description, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}