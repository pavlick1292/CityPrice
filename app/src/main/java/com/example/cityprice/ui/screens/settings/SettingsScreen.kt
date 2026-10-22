package com.example.cityprice.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityprice.data.repository.ThemeMode

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.themeMode.collectAsState()
    var showCityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Настройки",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Город
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                headlineContent = { Text("Город") },
                supportingContent = { Text("Партизанск") },
                modifier = Modifier.selectable(
                    selected = false,
                    onClick = { showCityDialog = true },
                    role = Role.Button
                )
            )
        }

        // Тема
        Column {
            Text("Тема оформления", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.selectableGroup()) {
                    ThemeOptionRow("Системная", ThemeMode.SYSTEM, currentTheme, viewModel::setTheme)
                    ThemeOptionRow("Светлая", ThemeMode.LIGHT, currentTheme, viewModel::setTheme)
                    ThemeOptionRow("Тёмная", ThemeMode.DARK, currentTheme, viewModel::setTheme)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            "CityPrice v1.0.0",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Город") },
            text = { Text("В MVP доступен только Партизанск. Выбор других городов появится позже.") },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("OK") }
            }
        )
    }
}

@Composable
private fun ThemeOptionRow(
    label: String,
    mode: ThemeMode,
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = (mode == current),
                onClick = { onSelect(mode) },
                role = Role.RadioButton
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (mode == current),
            onClick = null
        )
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}