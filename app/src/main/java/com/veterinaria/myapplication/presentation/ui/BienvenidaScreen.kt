package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/*
 * Pantalla de bienvenida que permite al usuario seleccionar el flujo de la aplicación.
 * @param onConsultaClick Navega al flujo de la Consulta (TutorScreen).
 * @param onFarmaciaClick Navega al flujo de la Farmacia (ClienteScreen).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BienvenidaScreen(
	onConsultaClick: () -> Unit,
	onFarmaciaClick: () -> Unit
) {
	Scaffold(
		topBar = {
			TopAppBar(title = { Text("Veterinaria - Servicios") })
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(32.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = "¡Bienvenido! Selecciona el tipo de servicio:",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(bottom = 40.dp)
			)
			
			// --- Opción 1: Consulta Veterinaria ---
			ServiceCard(
				title = "Consulta y Cita Veterinaria",
				description = "Registro de tutor, mascota, y cálculo de dosis/costo.",
				icon = Icons.Default.Pets,
				onClick = onConsultaClick,
				color = MaterialTheme.colorScheme.primaryContainer
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			// --- Opción 2: Farmacia y Pedidos ---
			ServiceCard(
				title = "Farmacia y Pedidos de Medicamentos",
				description = "Registro de cliente, elección de fármacos y descuentos.",
				icon = Icons.Default.LocalPharmacy,
				onClick = onFarmaciaClick,
				color = MaterialTheme.colorScheme.tertiaryContainer
			)
		}
	}
}

@Composable
fun ServiceCard(
	title: String,
	description: String,
	icon: androidx.compose.ui.graphics.vector.ImageVector,
	onClick: () -> Unit,
	color: androidx.compose.ui.graphics.Color
) {
	Card(
		onClick = onClick,
		modifier = Modifier.fillMaxWidth().height(160.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
	) {
		Row(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				icon,
				contentDescription = title,
				modifier = Modifier.size(48.dp).padding(end = 16.dp),
				tint = MaterialTheme.colorScheme.onSurface
			)
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = description,
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
			Icon(
				Icons.AutoMirrored.Filled.ArrowForward,
				contentDescription = null,
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
	}
}