package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.veterinaria.myapplication.presentation.viewmodel.ConsultaViewModel

/*
* Pantalla para capturar los datos del Tutor (Paso 1).
* Reemplaza la entrada de datos secuencial de la consola.
* * @param onNextClick Función de navegación a la siguiente pantalla (MascotaScreen).
* @param viewModel La instancia de ConsultaViewModel.
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
  onNextClick: () -> Unit,
  // Compose proporciona automáticamente la única instancia del ViewModel
  viewModel: ConsultaViewModel = viewModel()
) {
  Scaffold(
    topBar = {
      TopAppBar(title = { Text("Paso 1: Datos del Tutor") })
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
        text = "Ingresa los datos del Tutor de la mascota",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
      )

      // Campo: Nombre del Tutor
      OutlinedTextField(
        value = viewModel.nombreInput,
        onValueChange = viewModel::onNombreChange,
        label = { Text("Nombre del Tutor (Opcional)") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
      )

      // Campo: Teléfono de Contacto (Requerido: 12 dígitos puros)
      OutlinedTextField(
        value = viewModel.telefonoInput,
        onValueChange = viewModel::onTelefonoChange,
        label = { Text("Teléfono de Contacto (12 dígitos)") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Teléfono") },
        // isError se activa si el error no es nulo
        isError = viewModel.errorTelefono != null,
        supportingText = {
          val error = viewModel.errorTelefono
          if (error != null) {
            // Muestra el error en rojo
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

      // Botón Siguiente: Habilitado solo si el ViewModel confirma la validez
      Button(
        // La propiedad 'enabled' está ligada al estado 'esTutorValido' del ViewModel
        enabled = viewModel.esTutorValido,
        onClick = {
          // Si el ViewModel guarda el objeto Tutor, navegamos
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
