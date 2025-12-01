package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.data.TipoConsulta
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	onOpenDrawer: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	var showMenu by remember { mutableStateOf(false) }
	
	LaunchedEffect(Unit) {
		viewModel.inicializarDatosConsulta()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Consulta Veterinaria", fontWeight = FontWeight(700)) },
				navigationIcon = {
					IconButton(onClick = onBackClick) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
					}
				},
				actions = {
					IconButton(onClick = onOpenDrawer) {
						Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
					}
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
				.padding(horizontal = 16.dp, vertical = 24.dp),
		) {
			Text(
				text = "Seleccione los detalles de la atención.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp).align(Alignment.CenterHorizontally)
			)
			
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
							onClick = null
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
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth()
			)
			
			Spacer(modifier = Modifier.weight(1f))
			
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