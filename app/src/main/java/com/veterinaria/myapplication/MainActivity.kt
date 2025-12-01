package com.veterinaria.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.veterinaria.myapplication.presentation.ui.BienvenidaScreen
import com.veterinaria.myapplication.presentation.ui.ConsultaScreen
import com.veterinaria.myapplication.presentation.ui.MascotaScreen
import com.veterinaria.myapplication.presentation.ui.ResumenScreen
import com.veterinaria.myapplication.presentation.ui.TutorScreen
import com.veterinaria.myapplication.presentation.ui.ClienteScreen
import com.veterinaria.myapplication.presentation.ui.MedicamentoScreen
import com.veterinaria.myapplication.presentation.ui.MenuDrawer
import com.veterinaria.myapplication.presentation.ui.ResumenFarmaciaScreen
import com.veterinaria.myapplication.presentation.ui.SplashScreen
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel
import com.veterinaria.myapplication.ui.theme.VeterinariaTheme
import kotlinx.coroutines.launch

object VeterinariaRoutes {
	const val SPLASH = "splash"
	const val BIENVENIDA = "Bienvenida"
	const val TUTOR = "tutor"
	const val MASCOTA = "mascota"
	const val CONSULTA = "consulta"
	const val RESUMEN_CONSULTA = "resumenConsulta"
	
	
	// Flujo de Farmacia
	const val CLIENTE = "cliente"
	const val MEDICAMENTO = "medicamento"
	const val RESUMEN_FARMACIA = "resumenFarmacia"
}
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			// Tema del proyecto
			VeterinariaTheme {
				// Surface es el contenedor principal de la UI
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					VeterinariaApp()
					
				}
			}
		}
	}
}

@Composable
fun VeterinariaApp() {
	// 1. Inicializar el controlador de navegación
	val navController = rememberNavController()
	
	// Determinar la ruta actual para el ítem seleccionado en el Drawer
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	
	// 2. Estado del Drawer (Abrir/Cerrar) y Coroutine Scope
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()
	
	// Función para abrir el drawer (llamada por el botón de menú en el TopAppBar)
	val onOpenDrawer: () -> Unit = {
		scope.launch { drawerState.open() }
	}
	
	// 3. Crear una única instancia del ViewModel para toda la navegación (Scoped ViewModel)
	// Esta instancia sobrevive a los cambios de configuración.
	// El ViewModel de Consulta solo gestiona el flujo de Consultas
	val consultaViewModel: ConsultaViewModel = viewModel()
	val farmaciaViewModel: FarmaciaViewModel = viewModel()
	
	// La app completa está envuelta en el ModalNavigationDrawer
	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			MenuDrawer(
				currentRoute = currentRoute,
				onCloseDrawer = {
					scope.launch { drawerState.close() }
				},
				onNavigate = { route ->
					// Navega a la nueva ruta y limpia la pila hasta el menú principal
					navController.popBackStack(VeterinariaRoutes.BIENVENIDA, inclusive = false)
					navController.navigate(route)
				}
			)
		}
	) {
		// 4. Contenedor de navegación (NavHost)
		NavHost(
			navController = navController,
			startDestination = VeterinariaRoutes.SPLASH,
			enterTransition = { fadeIn(animationSpec = tween(1500)) },
			exitTransition = { fadeOut(animationSpec = tween(1500)) },
			popEnterTransition = { fadeIn(animationSpec = tween(1500)) },
			popExitTransition = { fadeOut(animationSpec = tween(1500)) }
		) {
			
			// --- 0. SPLASH SCREEN (NUEVA IMPLEMENTACIÓN) ---
			composable(
				VeterinariaRoutes.SPLASH
			) {
				SplashScreen(
					onTimeOut = {
						// Navega al menú principal y limpia la pila para que no se pueda volver al splash
						navController.popBackStack()
						navController.navigate(VeterinariaRoutes.BIENVENIDA)
					}
				)
			}
			
			// --- 1. BIENVENIDA ---
			composable(VeterinariaRoutes.BIENVENIDA) {
				BienvenidaScreen(
//					onConsultaClick = { navController.navigate(VeterinariaRoutes.TUTOR) },
//					onFarmaciaClick = { navController.navigate(VeterinariaRoutes.CLIENTE) },
					onOpenDrawer = onOpenDrawer // Se añade para permitir abrir el drawer
				)
			}
			
			// --- 2. TUTOR SCREEN ---
			composable(VeterinariaRoutes.TUTOR) {
				TutorScreen(
					viewModel = consultaViewModel,
					onNextClick = { navController.navigate(VeterinariaRoutes.MASCOTA) },
					onOpenDrawer = onOpenDrawer
				)
				
			}
			
			// --- 3. MASCOTA SCREEN ---
			composable(VeterinariaRoutes.MASCOTA) {
				MascotaScreen(
					viewModel = consultaViewModel,
					onBackClick = { navController.popBackStack() },
					onNextClick = { navController.navigate(VeterinariaRoutes.CONSULTA) },
					onOpenDrawer = onOpenDrawer
				)
			}
			
			// --- 4. CONSULTA SCREEN  ---
			composable(VeterinariaRoutes.CONSULTA) {
				ConsultaScreen(
					viewModel = consultaViewModel,
					onBackClick = { navController.popBackStack() },
					onNextClick = { navController.navigate(VeterinariaRoutes.RESUMEN_CONSULTA) },
					onOpenDrawer = onOpenDrawer
				)
			}
			
			// --- 5. RESUMEN SCREEN ---
			composable(VeterinariaRoutes.RESUMEN_CONSULTA) {
				ResumenScreen(
					viewModel = consultaViewModel,
					// Los Resumenes no usan Drawer, pero al volver aquí queremos ir al inicio
					onDoneClick = {
						navController.popBackStack(VeterinariaRoutes.BIENVENIDA, inclusive = false)
					}
				)
			}
			
			// ---------------------------------------------------------------------
			// --- FLUJO DE FARMACIA (Cliente -> Medicamento -> Resumen) ---
			// ---------------------------------------------------------------------
			
			// --- 6. CLIENTE SCREEN (Implementado) ---
			composable(VeterinariaRoutes.CLIENTE) {
				ClienteScreen(
					viewModel = farmaciaViewModel,
					onBackClick = {
						navController.popBackStack(VeterinariaRoutes.BIENVENIDA, inclusive = false)
					},
					onNextClick = {
						navController.navigate(VeterinariaRoutes.MEDICAMENTO)
					},
					onOpenDrawer = onOpenDrawer
				)
			}
			
			// --- 7. MEDICAMENTO SCREEN (Pendiente) ---
			composable(VeterinariaRoutes.MEDICAMENTO) {
				MedicamentoScreen(
					viewModel = farmaciaViewModel,
					onBackClick = {
						navController.popBackStack()
					},
					onNextClick = {
						navController.navigate(VeterinariaRoutes.RESUMEN_FARMACIA)
					},
					onOpenDrawer = onOpenDrawer
				)
			}
			
			// --- 8. RESUMEN FARMACIA SCREEN ---
			composable(VeterinariaRoutes.RESUMEN_FARMACIA) {
				ResumenFarmaciaScreen(
					viewModel = farmaciaViewModel,
					onDoneClick = {
						navController.popBackStack(VeterinariaRoutes.BIENVENIDA, inclusive = false)
					}
				)
			}
		}
	}
}
