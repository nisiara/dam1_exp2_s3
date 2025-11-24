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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.Medicamento
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties


/*
 * Pantalla para mostrar el resumen final del pedido de farmacia y las funcionalidades avanzadas
 * (Combinación de Pedidos y Reflection).
 *
 * @param onDoneClick Función para volver al menú de Bienvenida.
 * @param viewModel La instancia compartida de FarmaciaViewModel.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenFarmaciaScreen(
	onDoneClick: () -> Unit,
	viewModel: FarmaciaViewModel = viewModel()
) {
	val pedido = viewModel.pedidoActual
	val cliente = pedido?.cliente
	val medicamento = pedido?.medicamento
	
	// Formateador de moneda
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Resumen de Pedido de Farmacia") },
				navigationIcon = {
					IconButton(onClick = onDoneClick) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al inicio")
					}
				}
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(horizontal = 16.dp, vertical = 24.dp)
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// Verificación de datos esenciales
			if (pedido == null || cliente == null || medicamento == null) {
				Text("Error: El pedido no se pudo cargar.", color = MaterialTheme.colorScheme.error)
				Spacer(Modifier.height(16.dp))
				Button(onClick = onDoneClick) { Text("Volver") }
				return@Column
			}
			
			// --- Título y Total ---
			Card(
				colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
				modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
			) {
				Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
					Text("TOTAL DEL PEDIDO ACTUAL", style = MaterialTheme.typography.titleMedium)
					Text(
						text = currencyFormat.format(pedido.total),
						style = MaterialTheme.typography.headlineLarge,
						fontWeight = FontWeight.ExtraBold,
						color = MaterialTheme.colorScheme.tertiary
					)
					Text(
						text = "Precio normal: ${currencyFormat.format(pedido.precioNormal)}",
						style = MaterialTheme.typography.bodySmall
					)
				}
			}
			
			// --- Sección 1: Detalles del Pedido ---
			ResumenFarmaciaSection(title = "Detalles del Producto") {
				ResumenRow(Icons.Default.LocalPharmacy, "Producto:", medicamento.nombre)
				ResumenRow(Icons.Default.Category, "Tipo:", medicamento.tipo)
				ResumenRow(Icons.Default.Percent, "Descuento Aplicado:", "${(viewModel.descuentoAplicado * 100).toInt()}%")
				
				Spacer(modifier = Modifier.height(8.dp))
				Divider()
				Spacer(modifier = Modifier.height(8.dp))
				
				Text("Información del Cliente:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
				ResumenRow(Icons.Default.Person, "Nombre:", cliente.nombre)
				ResumenRow(Icons.Default.Phone, "Teléfono:", cliente.telefono)
				ResumenRow(Icons.Default.Email, "Email:", cliente.email)
			}
			
			// --- Sección 2: Combinación de Pedidos (Lógica Avanzada) ---
			PedidoCombinadoSection(viewModel = viewModel)
			
			// --- Sección 3: Reflection de Kotlin (Lógica Avanzada) ---
			ReflectionSection(medicamento = medicamento)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			// Botón de finalización
			Button(
				onClick = onDoneClick,
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Volver al Inicio")
			}
		}
	}
}

/**
 * Muestra la funcionalidad de combinación de pedidos, migrada del operador '+'.
 */
@Composable
fun PedidoCombinadoSection(viewModel: FarmaciaViewModel) {
	// Usamos el operador de combinación directamente en el ViewModel
	val pedidoCombinado = viewModel.combinarPedidos()
	//Combinación (Operador '+')
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
						
						// Aquí usamos la lógica de sobrecarga de los modelos Cliente y Medicamento
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
		/*
		else {
			Text("Necesitas al menos 2 pedidos para usar la función de combinación ('operator +').")
		}
	
		 */
	}
}

/*
 * Muestra el listado de propiedades y métodos de la clase Medicamento (Reflection).
 */
@Composable
fun ReflectionSection(medicamento: Medicamento) {
	ResumenFarmaciaSection(title = "Análisis por Reflection de Kotlin") {
		val kClass = medicamento::class
		
		Text(
			text = "Clase Analizada: ${kClass.simpleName}",
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		
		Divider()
		
		// Propiedades
		Text("Propiedades:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
		kClass.declaredMemberProperties.forEach { prop ->
			val value = remember(prop) { runCatching { prop.getter.call(medicamento)?.toString() }.getOrNull() ?: "N/A" }
			ResumenRow(Icons.Default.Tag, "- ${prop.name}:", value) // Usamos ResumenRow con ícono genérico
		}
		
		// Métodos
		Spacer(modifier = Modifier.height(12.dp))
		Text("Lista Métodos:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
		kClass.declaredMemberFunctions
			.filter { it.name !in listOf("equals", "hashCode", "toString", "copy") } // Filtramos métodos comunes
			.forEach { fn ->
				val paramsDesc = fn.parameters.drop(1).joinToString(", ") { it.type.toString() }
				ResumenRow(Icons.Default.Functions, "- ${fn.name}:", "(${paramsDesc})") // Usamos ResumenRow con ícono genérico
			}
	}
}