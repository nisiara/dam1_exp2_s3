package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

/*
 * Pantalla para capturar los datos de la Mascota (Paso 2).
 * Incluye validación de Edad (Int > 0) y Peso (Double > 0.0).
 *
 * @param onNextClick Función de navegación a la siguiente pantalla (ConsultaScreen).
 * @param onBackClick Función de navegación para volver a la pantalla del Tutor.
 * @param viewModel La instancia de ConsultaViewModel.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Paso 2: Datos de la Mascota") },
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
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Ingresa los datos de las mascota.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp)
			)
			
			// Campo: Nombre de la Mascota
			OutlinedTextField(
				value = viewModel.nombreMascotaInput,
				onValueChange = viewModel::onNombreMascotaChange,
				label = { Text("Nombre de la Mascota") },
				leadingIcon = { Icon(Icons.Filled.Pets, contentDescription = "Nombre Mascota") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			// Campo: Especie de la Mascota
			OutlinedTextField(
				value = viewModel.especieMascotaInput,
				onValueChange = viewModel::onEspecieMascotaChange,
				label = { Text("Especie (Ej: Perro, Gato)") },
				leadingIcon = { Icon(Icons.Default.Science, contentDescription = "Especie") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			// Campo: Edad
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
			
			// Campo: Peso (Usando Double para precisión)
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
						Text("Ej: 2.5 (El peso es crucial para la dosis).")
					}
				},
				// Usamos NumberDecimal para permitir el punto decimal
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			// Botón Siguiente
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
