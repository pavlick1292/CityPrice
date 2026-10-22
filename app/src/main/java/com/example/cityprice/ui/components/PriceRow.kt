package com.example.cityprice.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Строка «Магазин — Цена». Если isBest = true, подсвечиваем зелёным и добавляем «выгодно».
 */
@Composable
fun PriceRow(store: String, price: Int, isBest: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isBest) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = store,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        if (isBest) {
            Text(
                "выгодно",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = "$price ₽",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isBest) FontWeight.Bold else FontWeight.Normal,
            color = if (isBest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}