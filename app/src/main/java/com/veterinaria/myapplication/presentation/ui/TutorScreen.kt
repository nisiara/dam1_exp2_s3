package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
	onNextClick: () -> Unit,
	onOpenDrawer: () -> Unit,
	viewModel: ConsultaViewModel = viewModel()
) {
	var showMenu by remember { mutableStateOf(false) }
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Consulta Veterinaria", fontWeight = FontWeight(700)) },
				navigationIcon = {
					IconButton(onClick = onOpenDrawer) {
						Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
					}
				},
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
				.padding(horizontal = 16.dp, vertical = 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Datos del Tutor de la mascota",
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Left,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 24.dp)
			)
			
			OutlinedTextField(
				value = viewModel.nombreInput,
				onValueChange = viewModel::onNombreChange,
				label = { Text("Nombre del Tutor (Opcional)") },
				leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			OutlinedTextField(
				value = viewModel.telefonoInput,
				onValueChange = viewModel::onTelefonoChange,
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
			
			OutlinedTextField(
				value = viewModel.emailInput,
				onValueChange = viewModel::onEmailChange,
				label = { Text("Correo Electrónico") },
				leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
				isError = viewModel.errorEmail != null,
				supportingText = {
					val error = viewModel.errorEmail
					if (error != null) {
						Text(text = error, color = MaterialTheme.colorScheme.error)
					} else {
						Text("Ej: tunombre@dominio.cl")
					}
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			Button(
				enabled = viewModel.esTutorValido,
				onClick = {
					if (viewModel.guardarTutor()) {
						onNextClick()
					}
				},
				modifier = Modifier.fillMaxWidth().height(50.dp)
			) {
				Text("Datos de la Mascota")
				Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Siguiente", Modifier.padding(start = 8.dp))
			}
		}
	}
}
