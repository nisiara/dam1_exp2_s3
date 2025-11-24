package com.veterinaria.myapplication.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.veterinaria.myapplication.data.Consulta
import com.veterinaria.myapplication.data.Mascota
import com.veterinaria.myapplication.data.TipoConsulta
import com.veterinaria.myapplication.data.Tutor
import com.veterinaria.myapplication.domain.AsignarVeterinario
import com.veterinaria.myapplication.domain.CalcularCosto
// Se importan las funciones directamente, ya que no pertenecen a una clase.
import com.veterinaria.myapplication.domain.ValidationUtils.formatearTelefono
import com.veterinaria.myapplication.domain.ValidationUtils.getNombreTutor
import com.veterinaria.myapplication.domain.ValidationUtils.validarEmail
import com.veterinaria.myapplication.domain.ValidationUtils.validarTelefono

import com.veterinaria.myapplication.domain.CalcularDosis
import com.veterinaria.myapplication.domain.PlanificarVacuna


/**
 * ViewModel para el flujo de la consulta veterinaria.
 *
 * RESPONSABILIDAD: Siguiendo el patrón MVVM (Model-View-ViewModel), esta clase no conoce la UI (la Vista),
 * pero expone el estado que la UI debe mostrar y los métodos para que la UI le notifique eventos (acciones del usuario).
 * Su principal responsabilidad es gestionar la lógica de negocio y el estado de la pantalla.
 *
 * CICLO DE VIDA: Al extender de `ViewModel`, Android se encarga de que esta clase sobreviva a cambios
 * de configuración (como rotar el teléfono), manteniendo todos los datos intactos sin necesidad de guardarlos y restaurarlos manualmente.
 */
class ConsultaViewModel : ViewModel() {

    private var nextConsultaId = 1 // Contador simple para el ID de la consulta

	// --- ESTADO OBSERVABLE PARA LA UI ---

	/**
	 * Almacena el objeto `Tutor` final y validado. Es el "Model" que resulta de este flujo.
	 * Es `null` inicialmente.
	 * `private set` es crucial para seguir un patrón de Flujo de Datos Unidireccional (UDF).
	 * Significa que la Vista (UI) puede leer (`get`) el valor de `tutor`, pero no puede modificarlo (`set`) directamente.
	 * La modificación solo puede ocurrir DENTRO de este ViewModel.
	 */
	var tutor: Tutor? by mutableStateOf(null)
		private set

	/**
	 * Almacena el texto que el usuario escribe en el campo "Nombre".
	 * `by mutableStateOf("")` es un delegado de propiedad de Compose. Convierte una variable normal
	 * en un objeto de estado (`MutableState`). Cuando su valor cambia, le notifica a Compose que
	 * cualquier Composable que esté leyendo este estado debe ser "recompuesto" (redibujado en la pantalla).
	 * `private set` fuerza a la UI a usar el método `onNombreChange` para actualizar el valor.
	 */

	// ---------------------------------------------------------------------------------------------
	// --- PASO 1: TUTOR ---
	// ---------------------------------------------------------------------------------------------
	var nombreInput by mutableStateOf("")
		private set

	/**
	 * Almacena el texto que el usuario escribe en el campo "Teléfono".
	 */
	var telefonoInput by mutableStateOf("")
		private set

	/**
	 Almacena el texto que el usuario escribe en el campo "Email".
	*/
	var emailInput by mutableStateOf("")
		private set

	// --- ESTADO DE LOS ERRORES PARA LA UI ---

	/**
	 * Almacena el mensaje de error para el campo de teléfono. Si es `null`, no hay error.
	 * La UI leerá este estado para decidir si debe mostrar un mensaje de error y pintar el campo en rojo.
	 */
	var errorTelefono by mutableStateOf<String?>(null)
		private set

	/**
	 * Almacena el mensaje de error para el campo de email. Si es `null`, no hay error.
	 */
	var errorEmail by mutableStateOf<String?>(null)
		private set

	/**
	 * Es una propiedad computada (`get()`). No almacena un valor, sino que lo calcula cada vez que se accede a ella.
	 * Esto asegura que la validez del formulario siempre esté actualizada basándose en el estado más reciente
	 * de los `inputs`. La UI usará este booleano para, por ejemplo, habilitar o deshabilitar el botón "Siguiente".
	 */
	val esTutorValido: Boolean
		get() = validarTelefono(telefonoInput) &&
				validarEmail(emailInput) &&
				telefonoInput.isNotEmpty() && // Campo obligatorio
				emailInput.isNotEmpty()       // Campo obligatorio

	// --- MANEJO DE EVENTOS (Notificados por la UI) ---

	/**
	 * Event handler. La UI llama a esta función cuando el usuario escribe en el campo de nombre.
	 * Encapsula la lógica de cómo debe cambiar el estado `nombreInput`.
	 * @param newValue El nuevo texto que proviene del `OutlinedTextField` del nombre.
	 */
	fun onNombreChange(newValue: String) {
		nombreInput = newValue
	}

	/**
	 * Event handler para el campo de teléfono.
	 * Contiene lógica de pre-validación (filtro) y validación reactiva.
	 */
	fun onTelefonoChange(newValue: String) {
		// Filtro: Solo se actualiza el estado si el nuevo valor son dígitos y no excede la longitud.
		if (newValue.length <= 12 && newValue.all { it.isDigit() }) {
			telefonoInput = newValue
			// Validación reactiva: Inmediatamente después de cambiar el estado, se recalcula el error.
			errorTelefono = if (newValue.isEmpty()) {
				"El teléfono es obligatorio."
			} else if (!validarTelefono(newValue)) {
				"El teléfono debe tener exactamente 12 dígitos (Ej: 569123456780)."
			} else {
				null // Si es válido, se limpia el error.
			}
		}
	}

	/**
	 * Event handler para el campo de email.
	 */
	fun onEmailChange(newValue: String) {
		emailInput = newValue.trim() // Lógica de negocio: quitar espacios al inicio y final.
		// Validación reactiva para el correo.
		errorEmail = if (newValue.isEmpty()) {
			"El correo es obligatorio."
		} else if (!validarEmail(newValue)) {
			"El correo debe tener el formato correcto (ej: nombre@dominio.com)."
		} else {
			null // Es válido, no hay error.
		}
	}

	/**
	 * Acción final que se ejecuta cuando la UI notifica que el usuario ha presionado el botón de confirmación.
	 * Orquesta la lógica de negocio final antes de dar por terminado este paso del flujo.
	 * @return `true` si la operación fue exitosa, lo que le indica a la UI que puede proceder (navegar).
	 */
	fun guardarTutor(): Boolean {
		// Es una buena práctica volver a validar, aunque la UI debería haber prevenido la llamada con el botón deshabilitado.
		if (esTutorValido) {
			// Lógica de negocio final: aplicar formato y valores por defecto.
			val nombreFinal = getNombreTutor(nombreInput)
			val telefonoFormateado = formatearTelefono(telefonoInput)

			// Se crea el objeto de datos (el "Model") con los datos limpios y validados.
			tutor = Tutor(
				nombre = nombreFinal,
				telefono = telefonoFormateado,
				email = emailInput
			)
			return true // Notifica éxito.
		}
		return false // Notifica fallo.
	}


	// ---------------------------------------------------------------------------------------------
	// --- PASO 2: MASCOTA ---
	// ---------------------------------------------------------------------------------------------
	var mascota: Mascota? by mutableStateOf(null)
		private set

	// Inputs
	var nombreMascotaInput by mutableStateOf("")
		private set
	var especieMascotaInput by mutableStateOf("")
		private set
	var edadMascotaInput by mutableStateOf("") // String para el TextField
		private set
	var pesoMascotaInput by mutableStateOf("") // String para el TextField (soporta Double)
		private set

	// Resultados de la lógica de negocio (Calculados al guardar)
	var dosisVacuna: Double? by mutableStateOf(null)
		private set
	var mensajeNecesidadVacuna: String by mutableStateOf("")
		private set
	var proximaVacunaFecha: String by mutableStateOf("")
		private set

	// Errores
	var errorEdad by mutableStateOf<String?>(null)
		private set
	var errorPeso by mutableStateOf<String?>(null)
		private set

	// Propiedades computadas para la conversión segura
	private val edadInt: Int?
		get() = edadMascotaInput.toIntOrNull()

	private val pesoDouble: Double?
		get() = pesoMascotaInput.toDoubleOrNull()

	// Validez de Mascota: Todos los campos deben ser válidos y mayores a cero
	val esMascotaValida: Boolean
		get() = edadInt != null && edadInt!! > 0 &&
				pesoDouble != null && pesoDouble!! > 0.0 &&
				nombreMascotaInput.isNotBlank() && // Nombre no puede estar en blanco (aunque se use un default)
				especieMascotaInput.isNotBlank()


	fun onNombreMascotaChange(newValue: String) {
		nombreMascotaInput = newValue
	}

	fun onEspecieMascotaChange(newValue: String) {
		especieMascotaInput = newValue
	}

	fun onEdadMascotaChange(newValue: String) {
		// Filtro: Solo acepta dígitos
		edadMascotaInput = newValue.filter { it.isDigit() }
		val edad = edadInt
		errorEdad = if (edad == null || edad <= 0) {
			"La edad debe ser un número entero mayor a 0."
		} else {
			null
		}
	}

	fun onPesoMascotaChange(newValue: String) {
		// Filtro: Permite dígitos y solo un punto decimal, limpiando comas
		val filteredValue = newValue.replace(',', '.').filterIndexed { index, char ->
			char.isDigit() || (char == '.' && index != 0 && newValue.indexOf('.') == index)
		}
		pesoMascotaInput = filteredValue

		val peso = pesoDouble
		errorPeso = if (peso == null || peso <= 0.0) {
			"El peso debe ser un número (con decimales) mayor a 0.0 kg."
		} else {
			null
		}
	}

	/**
	 * Acción final: Crea y almacena el objeto Mascota y ejecuta la lógica de cálculo.
	 */
	fun guardarMascota(): Boolean {
		if (esMascotaValida && tutor != null) {
			val edad = edadInt!!
			val peso = pesoDouble!!

			// Aplicar valor por defecto si el nombre/especie están en blanco (similar a tu Service)
			val nombreFinal = nombreMascotaInput.ifBlank { "Mascota sin nombre" }
			val especieFinal = especieMascotaInput.ifBlank { "Especie no especificada" }

			// 1. Ejecutar la lógica de negocio (Capa Domain)
			dosisVacuna = CalcularDosis.calcularDosisRecomendada(peso, edad)
			mensajeNecesidadVacuna = PlanificarVacuna.getMensajeNecesidadVacuna(edad)
			proximaVacunaFecha = PlanificarVacuna.calcularProximaVacuna(edad)

			// 2. Crear y almacenar el objeto Mascota
			mascota = Mascota(
				nombre = nombreFinal,
				especie = especieFinal,
				edad = edad,
				peso = peso,
				tutor = tutor!!
			)
			return true
		}
		return false
	}

	// ---------------------------------------------------------------------------------------------
	// --- PASO 3: CONSULTA ---
	// ---------------------------------------------------------------------------------------------

    var consulta: Consulta? by mutableStateOf(null)
        private set

	// Inputs
	var tipoConsultaSeleccionado by mutableStateOf<TipoConsulta?>(null)
		private set
	var horaInput by mutableStateOf("")
		private set

	// Errores
	var errorHora by mutableStateOf<String?>(null)
		private set

	// Valores calculados al inicio de la pantalla de Consulta
	var nombreVeterinarioAsignado by mutableStateOf<String?>(null)
		private set

	// Inicializa el veterinario tan pronto como se entra a la pantalla de Consulta
	fun inicializarDatosConsulta() {
		// Lógica de negocio: Asignar veterinario según el día de hoy
		nombreVeterinarioAsignado = AsignarVeterinario.asignarVeterinarioHoy()
	}

	fun onTipoConsultaChange(newTipo: TipoConsulta) {
		tipoConsultaSeleccionado = newTipo
	}

	fun onHoraChange(newValue: String) {
		horaInput = newValue.trim()
		errorHora = if (newValue.isEmpty()) {
			"La hora es obligatoria."
		} else if (AsignarVeterinario.esHoraOcupada(newValue)) {
			"La hora $newValue ya está ocupada. Intente con otro horario."
		} else {
			null
		}
	}

	// La consulta es válida si el tipo fue seleccionado y la hora no tiene errores
	val esConsultaValida: Boolean
		get() = tipoConsultaSeleccionado != null &&
				horaInput.isNotEmpty() &&
				errorHora == null


	/**
	 * Acción final: Crea y almacena el objeto Consulta y calcula el valor final.
	 */
	fun guardarConsulta(): Boolean {
		// Asegurarse de que todos los objetos de datos previos existan y los inputs actuales sean válidos
		if (tutor != null && mascota != null && esConsultaValida) {
			val tipo = tipoConsultaSeleccionado!!
			val hora = horaInput
			val veterinario = nombreVeterinarioAsignado ?: AsignarVeterinario.asignarVeterinarioHoy()

			// 1. Ejecutar la lógica de negocio (Capa Domain)
			val valorTotal = CalcularCosto.calcularValorTotal(tipo)

			// 2. Crear y almacenar el objeto Consulta
			consulta = Consulta(
				idConsulta = nextConsultaId++, // Usa el ID y lo incrementa
				tipo = tipo,
				valorTotal = valorTotal,
				hora = hora,
				mascota = mascota!!,
				nombreVeterinario = veterinario
			)
			return true
		}
		return false
	}






}
