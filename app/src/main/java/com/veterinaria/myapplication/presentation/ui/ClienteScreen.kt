package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel


/*
 * Pantalla para capturar los datos del Cliente (Flujo Farmacia, Paso 1).
 * Es similar al TutorScreen pero usa el FarmaciaViewModel.
 *
 * @param onNextClick Función de navegación a la siguiente pantalla (MedicamentoScreen).
 * @param onBackClick Función de navegación para volver a la pantalla de Bienvenida.
 * @param viewModel La instancia de FarmaciaViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
	onNextClick: () -> Unit,
	onBackClick: () -> Unit,
	viewModel: FarmaciaViewModel = viewModel() // Usamos el FarmaciaViewModel
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Farmacia: Datos del Cliente") },
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
				text = "Ingresa los datos del cliente para el pedido.",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp)
			)
			
			// Campo: Nombre del Cliente
			OutlinedTextField(
				value = viewModel.nombreClienteInput,
				onValueChange = viewModel::onNombreClienteChange,
				label = { Text("Nombre del Cliente (Opcional)") },
				leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			// Campo: Teléfono de Contacto (Requerido: 12 dígitos puros)
			OutlinedTextField(
				value = viewModel.telefonoClienteInput,
				onValueChange = viewModel::onTelefonoClienteChange,
				label = { Text("Teléfono de Contacto (12 dígitos)") },
				leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Teléfono") },
				isError = viewModel.errorTelefono != null,
				supportingText = {
					val error = viewModel.errorTelefono
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Ej: 569123456780 (sin espacios ni guiones)")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			// Campo: Correo Electrónico (Requerido: Formato válido)
			OutlinedTextField(
				value = viewModel.emailClienteInput,
				onValueChange = viewModel::onEmailClienteChange,
				label = { Text("Correo Electrónico") },
				leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
				isError = viewModel.errorEmail != null,
				supportingText = {
					val error = viewModel.errorEmail
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Ej: cliente@dominio.cl")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			// Botón Siguiente: Habilitado solo si el ViewModel confirma la validez
			Button(
				enabled = viewModel.esClienteValido,
				onClick = {
					if (viewModel.guardarCliente()) {
						onNextClick()
					}
				},
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Seleccionar Medicamento")
				Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Siguiente", Modifier.padding(start = 8.dp))
			}
		}
	}
}