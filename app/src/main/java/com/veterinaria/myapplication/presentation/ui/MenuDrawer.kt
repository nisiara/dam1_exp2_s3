package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.veterinaria.myapplication.VeterinariaRoutes

/**
 * Define un elemento en el menú lateral de navegación.
 */
data class DrawerItem(
	val route: String,
	val icon: androidx.compose.ui.graphics.vector.ImageVector,
	val label: String
)

/*
 * Componente ModalNavigationDrawer que contiene la estructura del menú lateral.
 *
 * @param currentRoute La ruta actual para saber qué ítem está seleccionado.
 * @param onNavigate Función que maneja la navegación cuando se selecciona un destino.
 * @param onCloseDrawer Función para cerrar el drawer después de la navegación.
 * @param content El contenido de la pantalla principal (NavHost).
 */
@Composable
fun MenuDrawer(
	currentRoute: String?,
	onNavigate: (String) -> Unit,
	onCloseDrawer: () -> Unit
) {
	// Definición de los ítems principales del menú
	val items = listOf(
		// Opción: Volver al menú de Bienvenida
		DrawerItem(VeterinariaRoutes.BIENVENIDA, Icons.Default.Home, "Inicio"),
		// Opción: Ir al inicio del flujo de Consulta
		DrawerItem(VeterinariaRoutes.TUTOR, Icons.Default.Pets, "Consulta Veterinaria"),
		// Opción: Ir al inicio del flujo de Farmacia
		DrawerItem(VeterinariaRoutes.CLIENTE, Icons.Default.LocalPharmacy, "Farmacia")
	)
	
	ModalDrawerSheet(
		modifier = Modifier.padding(end = 56.dp) // Deja un poco de espacio visible
	) {
		// Encabezado del Drawer
		Text(
			"Sana sana colita de rana",
			style = MaterialTheme.typography.headlineSmall,
			modifier = Modifier.padding(16.dp)
		)
		HorizontalDivider()
		Spacer(Modifier.height(8.dp))
		
		// Items de navegación
		items.forEach { item ->
			NavigationDrawerItem(
				icon = { Icon(item.icon, contentDescription = item.label) },
				label = { Text(item.label) },
				selected = currentRoute == item.route,
				
				onClick = {
					onNavigate(item.route)
					onCloseDrawer()
				},
				modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
			)
		}
	}
}