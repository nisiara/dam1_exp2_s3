package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.Medicamento
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel
import java.text.NumberFormat
import java.util.Locale

/*
 * Pantalla para capturar la selección del Medicamento (Flujo Farmacia, Paso 2).
 * Lista los medicamentos disponibles y muestra los cálculos de descuento en tiempo real.
 *
 * @param onNextClick Función de navegación a la siguiente pantalla (ResumenFarmacia).
 * @param onBackClick Función de navegación para volver a la pantalla de Cliente.
 * @param viewModel La instancia de FarmaciaViewModel.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentoScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	viewModel: FarmaciaViewModel = viewModel()
) {
	val medicamentos = viewModel.getMedicamentosDisponibles()
	val seleccionado = viewModel.medicamentoSeleccionado
	val precioFinal = viewModel.precioFinal
	
	// Formateador de moneda (ejemplo de Chile, usa el que prefieras)
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Farmacia: Elegir Medicamento") },
				navigationIcon = {
					IconButton(onClick = onBackClick) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
					}
				}
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(horizontal = 16.dp, vertical = 8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Selecciona el producto y revisa el descuento aplicado.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 16.dp)
			)
			
			// --- Lista de Medicamentos ---
			LazyColumn(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				items(medicamentos) { medicamento ->
					MedicamentoCard(
						medicamento = medicamento,
						seleccionado = medicamento == seleccionado,
						onClick = viewModel::onMedicamentoSelect
					)
				}
			}
			
			// --- Resumen de Selección y Descuento (Zona Reactiva) ---
			if (seleccionado != null) {
				Card(
					modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
					colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
					elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
				) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(
							text = "Pedido Actual:",
							style = MaterialTheme.typography.titleSmall,
							fontWeight = FontWeight.Bold
						)
						Divider(modifier = Modifier.padding(vertical = 4.dp))
						
						// Nombre del Medicamento
						Text(
							text = seleccionado.nombre,
							style = MaterialTheme.typography.headlineSmall,
							fontWeight = FontWeight.SemiBold
						)
						
						// Información de la Promoción
						if (viewModel.nombrePromocion != null) {
							Text(
								text = "Promoción: ${viewModel.nombrePromocion}",
								fontWeight = FontWeight.SemiBold
							)
							Text(
								text = "Precio Base: ${currencyFormat.format(seleccionado.precio)}",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						} else {
							Text(
								text = "No aplica promoción para tipo: ${seleccionado.tipo}",
								color = MaterialTheme.colorScheme.error
							)
						}
						
						// Precio Final
						Spacer(modifier = Modifier.height(8.dp))
						precioFinal?.let { final ->
							Text(
								text = "Precio Final: ${currencyFormat.format(final)}",
								style = MaterialTheme.typography.titleLarge,
								color = MaterialTheme.colorScheme.primary,
								fontWeight = FontWeight.ExtraBold
							)
						}
					}
				}
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
			// Botón Finalizar Pedido
			Button(
				enabled = seleccionado != null,
				onClick = {
					if (viewModel.finalizarPedido()) {
						onNextClick()
					}
				},
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Ver Resumen")
				Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Finalizar", Modifier.padding(start = 8.dp))
			}
		}
	}
}

@Composable
fun MedicamentoCard(
	medicamento: Medicamento,
	seleccionado: Boolean,
	onClick: (Medicamento) -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onClick(medicamento) },
		colors = CardDefaults.cardColors(
			containerColor = if (seleccionado) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
		),
		border = if (seleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				Icons.Default.LocalPharmacy,
				contentDescription = null,
				modifier = Modifier.size(32.dp).padding(end = 8.dp),
				tint = MaterialTheme.colorScheme.primary
			)
			Column(modifier = Modifier.weight(1f)) {
				Text(medicamento.nombre, style = MaterialTheme.typography.titleMedium)
				Text("Tipo: ${medicamento.tipo}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
			}
			Text(
				text = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(medicamento.precio),
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold
			)
		}
	}
}