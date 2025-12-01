package com.veterinaria.myapplication.presentation.ui

import ResumenFarmaciaSection
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.Medicamento
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenFarmaciaScreen(
	onDoneClick: () -> Unit,
	viewModel: FarmaciaViewModel = viewModel()
) {
	var isLoading by remember { mutableStateOf(true) }
	
	LaunchedEffect(Unit) {
		delay(3000)
		isLoading = false
	}
	
	val pedido = viewModel.pedidoActual
	val cliente = pedido?.cliente
	val medicamento = pedido?.medicamento
	
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Farmacia", fontWeight = FontWeight(700)) },
//				actions = { Text("Resumen") },
				navigationIcon = {
					IconButton(onClick = onDoneClick) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al inicio")
					}
				}
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			contentAlignment = Alignment.Center
		) {
			if (isLoading) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center
				) {
					CircularProgressIndicator()
					Spacer(modifier = Modifier.height(16.dp))
					Text("Cargando resumen pedido farmacia")
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 16.dp, vertical = 24.dp)
						.verticalScroll(rememberScrollState()),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					if (pedido == null || cliente == null || medicamento == null) {
						Text("Error: El pedido no se pudo cargar.", color = MaterialTheme.colorScheme.error)
						Spacer(Modifier.height(16.dp))
						Button(onClick = onDoneClick) { Text("Volver") }
						return@Column
					}
					
					Card(
						colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
						modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
					) {
						Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
							Text("TOTAL PEDIDO ACTUAL", style = MaterialTheme.typography.titleMedium)
							Text(
								text = currencyFormat.format(pedido.total),
								textAlign = TextAlign.Left,
								style = MaterialTheme.typography.headlineLarge,
								fontWeight = FontWeight.ExtraBold,
//								color = MaterialTheme.colorScheme.tertiary
							)
							Text(
								text = "Precio normal: ${currencyFormat.format(pedido.precioNormal)}",
								style = MaterialTheme.typography.bodySmall
							)
						}
					}
					
					ResumenFarmaciaSection(title = "Detalles del Producto") {
						ResumenRow(Icons.Default.LocalPharmacy, "Producto:", medicamento.nombre)
						ResumenRow(Icons.Default.Category, "Tipo:", medicamento.tipo)
						ResumenRow(Icons.Default.Percent, "Descuento Aplicado:", "${(viewModel.descuentoAplicado * 100).toInt()}%" )
						
						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))
						
						Text("Información del Cliente:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
						ResumenRow(Icons.Default.Person, "Nombre:", cliente.nombre)
						ResumenRow(Icons.Default.Phone, "Teléfono:", cliente.telefono)
						ResumenRow(Icons.Default.Email, "Email:", cliente.email)
					}
					
					PedidoCombinadoSection(viewModel = viewModel)
					
					ReflectionSection(medicamento = medicamento)
					
					Spacer(modifier = Modifier.height(32.dp))
					
					Button(
						onClick = onDoneClick,
						modifier = Modifier.fillMaxWidth().height(50.dp)
					) {
						Text("Volver al Inicio")
					}
				}
			}
		}
	}
}

@Composable
fun PedidoCombinadoSection(viewModel: FarmaciaViewModel) {
	val pedidoCombinado = viewModel.combinarPedidos()
	ResumenFarmaciaSection(title = "Lista Pedidos") {
		val totalPedidos = viewModel.pedidosRealizados.size
		
		Text(
			text = "Total de Pedidos Registrados: $totalPedidos",
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		
		if (totalPedidos >= 2) {
			pedidoCombinado?.let { combined ->
				
				Column(Modifier.padding(12.dp)) {
					Text("RESUMEN DE PEDIDOS COMBINADOS", fontWeight = FontWeight.Bold)
					HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
					
					ResumenRow(Icons.Default.Group, "Clientes Combinados:", combined.cliente.nombre)
					ResumenRow(Icons.Default.LocalPharmacy, "Productos Combinados:", combined.medicamento.nombre)
					
					HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
					Text(
						text = "Total Combinado: ${NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(combined.total)}",
						style = MaterialTheme.typography.titleLarge,
						fontWeight = FontWeight.ExtraBold
					)
				}
				
			}
		}
	}
}

@Composable
fun ReflectionSection(medicamento: Medicamento) {
	ResumenFarmaciaSection(title = "Análisis por Reflection de Kotlin") {
		val kClass = medicamento::class
		
		Text(
			text = "Clase Analizada: ${kClass.simpleName}",
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		
		HorizontalDivider()
		
		Text("Propiedades:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
		kClass.declaredMemberProperties.forEach { prop ->
			val value = remember(prop) { runCatching { prop.getter.call(medicamento)?.toString() }.getOrNull() ?: "N/A" }
			ResumenRow(Icons.Default.Tag, "- ${prop.name}:", value)
		}
		
		Spacer(modifier = Modifier.height(12.dp))
		Text("Lista Métodos:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
		kClass.declaredMemberFunctions
			.filter { it.name !in listOf("equals", "hashCode", "toString", "copy") }
			.forEach { fn ->
				val paramsDesc = fn.parameters.drop(1).joinToString(", ") { it.type.toString() }
				ResumenRow(Icons.Default.Functions, "- ${fn.name}:", "(${paramsDesc})")
			}
	}
}