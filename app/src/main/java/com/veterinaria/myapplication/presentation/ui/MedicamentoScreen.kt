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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentoScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	onOpenDrawer: () -> Unit,
	viewModel: FarmaciaViewModel = viewModel()
) {
	var showMenu by remember { mutableStateOf(false) }
	val medicamentos = viewModel.getMedicamentosDisponibles()
	val seleccionado = viewModel.medicamentoSeleccionado
	val precioFinal = viewModel.precioFinal
	
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Farmacia", fontWeight = FontWeight(700)) },
				navigationIcon = {
					IconButton(onClick = onOpenDrawer) {
						Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
					}
				},

//				navigationIcon = {
//					IconButton(onClick = onBackClick) {
//						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
//					}
//				},
				actions = {
					
					IconButton(onClick = { showMenu = true }) {
						Icon(Icons.Default.MoreVert, contentDescription = "Menú contextual")
					}
					DropdownMenu(
						expanded = showMenu,
						onDismissRequest = { showMenu = false }
					) {
						DropdownMenuItem(
							text = { Text("Iniciar sesión") },
							onClick = {
								showMenu = false
							}
						)
						DropdownMenuItem(
							text = { Text("Configuración") },
							onClick = {
								showMenu = false
							}
						)
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
				text = "Selecciona el medicamento.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 16.dp)
			)
			
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
			
			if (seleccionado != null) {
				Card(
					modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
					colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
//					elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
				) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(
							text = "Pedido Actual:",
							style = MaterialTheme.typography.titleSmall,
							fontWeight = FontWeight.Bold
						)
						HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
						
						Text(
							text = seleccionado.nombre,
							style = MaterialTheme.typography.headlineSmall,
							fontWeight = FontWeight.SemiBold
						)
						
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