package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.TipoConsulta
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

/**
 * Pantalla para capturar los datos de la Consulta (Paso 3).
 * Permite elegir el tipo de consulta, la hora y muestra el veterinario asignado.
 *
 * @param onNextClick Función de navegación a la siguiente pantalla (ResumenScreen).
 * @param onBackClick Función de navegación para volver a la pantalla de Mascota.
 * @param viewModel La instancia compartida de ConsultaViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	// Efecto secundario: Al entrar a la pantalla, inicializamos los datos (asignar veterinario)
	LaunchedEffect(Unit) {
		viewModel.inicializarDatosConsulta()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Paso 3: Datos de la Consulta") },
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
				.padding(horizontal = 16.dp, vertical = 24.dp),
		) {
			Text(
				text = "Seleccione los detalles de la atención.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp).align(Alignment.CenterHorizontally)
			)
			
			// --- Sección 1: Veterinario Asignado ---
			Card(
				colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
				modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
			) {
				Column(modifier = Modifier.padding(16.dp)) {
					Text(
						text = "Veterinario de Turno (Hoy)",
						style = MaterialTheme.typography.labelMedium
					)
					Text(
						text = viewModel.nombreVeterinarioAsignado ?: "Cargando...",
						style = MaterialTheme.typography.headlineSmall,
						fontWeight = FontWeight.Bold
					)
				}
			}
			
			// --- Sección 2: Tipo de Consulta (Radio Buttons) ---
			Text(
				text = "Tipo de Consulta",
				style = MaterialTheme.typography.titleSmall,
				modifier = Modifier.padding(bottom = 8.dp)
			)
			
			Column(modifier = Modifier.selectableGroup()) {
				TipoConsulta.entries.forEach { tipo ->
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.height(56.dp)
							.selectable(
								selected = (tipo == viewModel.tipoConsultaSeleccionado),
								onClick = { viewModel.onTipoConsultaChange(tipo) },
								role = Role.RadioButton
							)
							.padding(horizontal = 16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						RadioButton(
							selected = (tipo == viewModel.tipoConsultaSeleccionado),
							onClick = null // null recomendado para accesibilidad con selectable
						)
						Text(
							text = tipo.descripcion,
							style = MaterialTheme.typography.bodyLarge,
							modifier = Modifier.padding(start = 16.dp)
						)
					}
				}
			}
			
			if (viewModel.tipoConsultaSeleccionado == null) {
				Text(
					text = "Debe seleccionar una opción",
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodySmall,
					modifier = Modifier.padding(start = 16.dp)
				)
			}
			
			Spacer(modifier = Modifier.height(24.dp))
			
			// --- Sección 3: Hora ---
			OutlinedTextField(
				value = viewModel.horaInput,
				onValueChange = viewModel::onHoraChange,
				label = { Text("Hora de Atención (HH:MM)") },
				leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = "Hora") },
				isError = viewModel.errorHora != null,
				supportingText = {
					val error = viewModel.errorHora
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Horas ocupadas: 10:00, 11:30, 15:00")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Podría ser Number o DateTime
				modifier = Modifier.fillMaxWidth()
			)
			
			Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al final
			
			// Botón Finalizar
			Button(
				enabled = viewModel.esConsultaValida,
				onClick = {
					if (viewModel.guardarConsulta()) {
						onNextClick()
					}
				},
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Finalizar y Ver Resumen")
				Icon(Icons.Default.Check, contentDescription = "Finalizar", Modifier.padding(start = 8.dp))
			}
		}
	}
}