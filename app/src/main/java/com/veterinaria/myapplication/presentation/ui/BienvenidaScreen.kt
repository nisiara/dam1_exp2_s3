package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.veterinaria.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BienvenidaScreen(
//	onConsultaClick: () -> Unit,
//	onFarmaciaClick: () -> Unit,
	onOpenDrawer: () -> Unit
) {
	var showMenu by remember { mutableStateOf(false) }
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Bienvenido", fontWeight = FontWeight(700)) },
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
								// Acción de ejemplo
								showMenu = false
							}
						)
						DropdownMenuItem(
							text = { Text("Configuración") },
							onClick = {
								// Acción de ejemplo
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
				.padding(paddingValues),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Image(
				painter = painterResource(id = R.drawable.img_home),
				contentDescription = "Imagen de bienvenida",
				modifier = Modifier.fillMaxWidth()
			)
//			Column(
//			    modifier = Modifier.padding(16.dp),
//			    horizontalAlignment = Alignment.CenterHorizontally
//			) {
//				Spacer(modifier = Modifier.height(40.dp))
//
//				ServiceCard(
//					title = "Consulta y Cita Veterinaria",
//					description = "Registro de tutor, mascota, y cálculo de dosis/costo.",
//					icon = Icons.Default.Pets,
//					onClick = onConsultaClick,
//					color = MaterialTheme.colorScheme.primaryContainer
//				)
//
//				Spacer(modifier = Modifier.height(32.dp))
//
//				ServiceCard(
//					title = "Farmacia y Pedidos de Medicamentos",
//					description = "Registro de cliente, elección de fármacos y descuentos.",
//					icon = Icons.Default.LocalPharmacy,
//					onClick = onFarmaciaClick,
//					color = MaterialTheme.colorScheme.tertiaryContainer
//				)
//			}
		}
	}
}

@Composable
fun ServiceCard(
	title: String,
	description: String,
	icon: androidx.compose.ui.graphics.vector.ImageVector,
	onClick: () -> Unit,
	color: androidx.compose.ui.graphics.Color
) {
	Card(
		onClick = onClick,
		modifier = Modifier.fillMaxWidth().height(160.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
	) {
		Row(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				icon,
				contentDescription = title,
				modifier = Modifier.size(48.dp).padding(end = 16.dp),
				tint = MaterialTheme.colorScheme.onSurface
			)
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = description,
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
			Icon(
				Icons.AutoMirrored.Filled.ArrowForward,
				contentDescription = null,
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
	}
}