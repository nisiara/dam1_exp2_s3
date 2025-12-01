package com.veterinaria.myapplication.presentation.ui

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.TipoConsulta
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
	onDoneClick: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	var isLoading by remember { mutableStateOf(true) }
	
	LaunchedEffect(Unit) {
		delay(3000)
		isLoading = false
	}
	
	val tutor = viewModel.tutor
	val mascota = viewModel.mascota
	val consulta = viewModel.consulta
	
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Consulta Veterinaria", fontWeight = FontWeight(700)) },
				navigationIcon = {
					IconButton(onClick = onDoneClick) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al menú anterior")
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
						Text("Cargando resumen consulta")
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 16.dp, vertical = 24.dp)
						.verticalScroll(rememberScrollState()),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					if (tutor == null || mascota == null || consulta == null) {
						Text("Error: Faltan datos para mostrar el resumen.", color = MaterialTheme.colorScheme.error)
						Spacer(Modifier.height(16.dp))
						Button(onClick = onDoneClick) { Text("Volver") }
						return@Column
					}
					
					Card(
						colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
						modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
					) {
						Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
							Text("TOTAL A PAGAR", style = MaterialTheme.typography.titleMedium)
							Text(
								text = currencyFormat.format(consulta.valorTotal),
								style = MaterialTheme.typography.headlineLarge,
								fontWeight = FontWeight.ExtraBold,
								color = MaterialTheme.colorScheme.primary
							)
							Text("Consulta N° ${consulta.idConsulta}", style = MaterialTheme.typography.bodySmall)
						}
					}
					
					ResumenSection(title = "Detalles de la Consulta") {
						ResumenRow(Icons.Default.MedicalServices, "Veterinario Asignado:", consulta.nombreVeterinario)
						ResumenRow(Icons.Default.CalendarToday, "Tipo:", consulta.tipo.descripcion)
						ResumenRow(Icons.Default.Schedule, "Hora:", consulta.hora)
						
						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))
						
						if (consulta.tipo.descuento > 0.0) {
							ResumenRow(
								Icons.Default.Discount,
								"Descuento Aplicado:",
								"${(consulta.tipo.descuento * 100).toInt()}%"
							)
						}
						
						if (consulta.tipo != TipoConsulta.PELUQUERIA && consulta.tipo != TipoConsulta.OTRO) {
							Spacer(modifier = Modifier.height(16.dp))
							Text("Recomendaciones de Vacuna:", style = MaterialTheme.typography.titleMedium)
							
							viewModel.dosisVacuna?.let { dosis ->
								ResumenRow(
									Icons.Default.Medication,
									"Dosis Recomendada:",
									"${String.format("%.2f", dosis)} ml"
								)
							}
							
							ResumenRow(
								Icons.Default.DateRange,
								"Próxima Vacuna:",
								viewModel.proximaVacunaFecha
							)
							
							Text(
								text = viewModel.mensajeNecesidadVacuna,
								style = MaterialTheme.typography.bodySmall,
								modifier = Modifier.padding(start = 40.dp, top = 4.dp, bottom = 8.dp)
							)
						}
					}
					
					ResumenSection(title = "Datos de la Mascota") {
						ResumenRow(Icons.Default.Pets, "Nombre:", mascota.nombre)
						ResumenRow(Icons.Default.Science, "Especie:", mascota.especie)
						ResumenRow(Icons.Default.Cake, "Edad:", "${mascota.edad} años")
						ResumenRow(Icons.Default.Scale, "Peso:", "${String.format("%.1f", mascota.peso)} kg")
					}
					
					ResumenSection(title = "Datos del Tutor") {
						ResumenRow(Icons.Default.Person, "Nombre:", tutor.nombre)
						ResumenRow(Icons.Default.Phone, "Teléfono:", tutor.telefono)
						ResumenRow(Icons.Default.Email, "Email:", tutor.email)
					}
					
					Spacer(modifier = Modifier.height(32.dp))
					
					Button(
						onClick = onDoneClick,
						modifier = Modifier.fillMaxWidth().height(50.dp),
						colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
					) {
						Text("Volver al Inicio")
					}
				}
			}
		}
	}
}

/*
 * Componente helper para envolver cada sección de resumen.
 */
@Composable
fun ResumenSection(title: String, content: @Composable () -> Unit) {
	Column(
		modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.primary,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
		) {
			Column(Modifier.padding(16.dp)) {
				content()
			}
		}
	}
}

/*
 * Componente helper para mostrar una fila de detalle de resumen.
 */
@Composable
fun ResumenRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			icon,
			contentDescription = label,
			modifier = Modifier.size(20.dp).padding(end = 8.dp),
//			tint = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.SemiBold,
			modifier = Modifier.width(150.dp)
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.Normal
		)
	}
}