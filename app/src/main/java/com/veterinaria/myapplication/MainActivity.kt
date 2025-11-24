package com.veterinaria.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import com.veterinaria.myapplication.presentation.ui.BienvenidaScreen
import com.veterinaria.myapplication.presentation.ui.ConsultaScreen
import com.veterinaria.myapplication.presentation.ui.MascotaScreen
import com.veterinaria.myapplication.presentation.ui.ResumenScreen
import com.veterinaria.myapplication.presentation.ui.TutorScreen
import com.veterinaria.myapplication.presentation.ui.ClienteScreen
import com.veterinaria.myapplication.presentation.ui.MedicamentoScreen
import com.veterinaria.myapplication.presentation.ui.ResumenFarmaciaScreen
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel
import com.veterinaria.myapplication.presentation.viewmodel.FarmaciaViewModel
import com.veterinaria.myapplication.ui.theme.VeterinariaTheme

object VeterinariaRoutes {
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

  // 2. Crear una única instancia del ViewModel para toda la navegación (Scoped ViewModel)
  // Esta instancia sobrevive a los cambios de configuración.
	// El ViewModel de Consulta solo gestiona el flujo de Consultas
  val consultaViewModel: ConsultaViewModel = viewModel()
	val farmaciaViewModel: FarmaciaViewModel = viewModel()

  // 3. Contenedor de navegación (NavHost)
  NavHost(
    navController = navController,
    // La primera pantalla que se muestra
    startDestination = VeterinariaRoutes.BIENVENIDA
  ) {
		
		// --- 0. BIENVENIDA ---
		composable(VeterinariaRoutes.BIENVENIDA) {
			BienvenidaScreen(
				onConsultaClick = {
					navController.navigate(VeterinariaRoutes.TUTOR)
				},
				onFarmaciaClick = {
					navController.navigate(VeterinariaRoutes.CLIENTE)
				}
			)
		}
		
    // --- 1. TUTOR SCREEN ---
    composable(VeterinariaRoutes.TUTOR) {
      TutorScreen(
        // Pasamos la instancia del ViewModel
        viewModel = consultaViewModel,
        // Acción de navegación al presionar "Siguiente"
        onNextClick = {
          navController.navigate(VeterinariaRoutes.MASCOTA)
        }
      )
    }

    // --- 2. MASCOTA SCREEN ---
		composable(VeterinariaRoutes.MASCOTA) {
			MascotaScreen(
				viewModel = consultaViewModel,
				onBackClick = {
					// Vuelve a la pantalla del Tutor
					navController.popBackStack()
				},
				onNextClick = {
					// Navega a la pantalla de Consulta
					navController.navigate(VeterinariaRoutes.CONSULTA)
				}
			)
		}

    // --- 3. CONSULTA SCREEN (PENDIENTE DE IMPLEMENTAR) ---
		composable(VeterinariaRoutes.CONSULTA) {
			ConsultaScreen(
				viewModel = consultaViewModel,
				onBackClick = {
					navController.popBackStack()
				},
				onNextClick = {
					// Navega a la pantalla de Resumen
					navController.navigate(VeterinariaRoutes.RESUMEN_CONSULTA)
				}
			)
		}

    // --- 4. RESUMEN SCREEN ---
		composable(VeterinariaRoutes.RESUMEN_CONSULTA) {
			ResumenScreen(
				viewModel = consultaViewModel,
				// Al hacer 'onDoneClick', queremos volver al inicio (TUTOR),
				// vaciando la pila de navegación para que no puedan volver atrás.
				onDoneClick = {
					navController.popBackStack(VeterinariaRoutes.TUTOR, inclusive = false)
				}
			)
		}
		
		// ---------------------------------------------------------------------
		// --- FLUJO DE FARMACIA (Cliente -> Medicamento -> Resumen) ---
		// ---------------------------------------------------------------------
		
		// --- 5. CLIENTE SCREEN (Implementado) ---
		composable(VeterinariaRoutes.CLIENTE) {
			ClienteScreen(
				viewModel = farmaciaViewModel,
				onBackClick = {
					navController.popBackStack()
				},
				onNextClick = {
					navController.navigate(VeterinariaRoutes.MEDICAMENTO)
				}
			)
		}
		
		// --- 6. MEDICAMENTO SCREEN (Pendiente) ---
		composable(VeterinariaRoutes.MEDICAMENTO) {
			MedicamentoScreen(
				viewModel = farmaciaViewModel,
				onBackClick = {
					navController.popBackStack()
				},
				onNextClick = {
					navController.navigate(VeterinariaRoutes.RESUMEN_FARMACIA)
				}
			)
		}
		
		// --- 7. RESUMEN FARMACIA SCREEN ---
		composable(VeterinariaRoutes.RESUMEN_FARMACIA) {
			ResumenFarmaciaScreen( // <--- Conectamos la pantalla final
				viewModel = farmaciaViewModel,
				onDoneClick = {
					// Vuelve a la pantalla de Bienvenida, limpiando la pila del flujo de Farmacia
					navController.popBackStack(VeterinariaRoutes.BIENVENIDA, inclusive = false)
				}
			)
		}
  }
}
