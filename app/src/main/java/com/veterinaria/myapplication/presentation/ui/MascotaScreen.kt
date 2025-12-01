package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	onOpenDrawer: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	var showMenu by remember { mutableStateOf(false) }
	
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
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Ingresa los datos de las mascota.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp)
			)
			
			OutlinedTextField(
				value = viewModel.nombreMascotaInput,
				onValueChange = viewModel::onNombreMascotaChange,
				label = { Text("Nombre de la Mascota") },
				leadingIcon = { Icon(Icons.Filled.Pets, contentDescription = "Nombre Mascota") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			var expanded by remember { mutableStateOf(false) }
			val especies = listOf("Gato", "Perro", "Conejo", "Iguana", "Vaca", "Tiburon", "Cocodrilo")
			
			ExposedDropdownMenuBox(
				expanded = expanded,
				onExpandedChange = { expanded = !expanded },
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			) {
				OutlinedTextField(
					value = viewModel.especieMascotaInput,
					onValueChange = {},
					readOnly = true,
					label = { Text("Especie") },
					leadingIcon = { Icon(Icons.Default.Science, contentDescription = "Especie") },
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
					},
					modifier = Modifier.menuAnchor().fillMaxWidth()
				)
				ExposedDropdownMenu(
					expanded = expanded,
					onDismissRequest = { expanded = false }
				) {
					especies.forEach { especie ->
						DropdownMenuItem(
							text = { Text(especie) },
							onClick = {
								viewModel.onEspecieMascotaChange(especie)
								expanded = false
							}
						)
					}
				}
			}
			
			OutlinedTextField(
				value = viewModel.edadMascotaInput,
				onValueChange = viewModel::onEdadMascotaChange,
				label = { Text("Edad (Años)") },
				leadingIcon = { Icon(Icons.Default.Cake, contentDescription = "Edad") },
				isError = viewModel.errorEdad != null,
				supportingText = {
					val error = viewModel.errorEdad
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Debe ser un número entero positivo.")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			OutlinedTextField(
				value = viewModel.pesoMascotaInput,
				onValueChange = viewModel::onPesoMascotaChange,
				label = { Text("Peso (Kg, use punto decimal)") },
				leadingIcon = { Icon(Icons.Default.Scale, contentDescription = "Peso") },
				isError = viewModel.errorPeso != null,
				supportingText = {
					val error = viewModel.errorPeso
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Ej: 2.5")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			Button(
				enabled = viewModel.esMascotaValida,
				onClick = {
					if (viewModel.guardarMascota()) {
						onNextClick()
					}
				},
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Detalles de la Consulta")
				Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Siguiente", Modifier.padding(start = 8.dp))
			}
		}
	}
}