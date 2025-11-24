package com.veterinaria.myapplication.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.veterinaria.myapplication.data.Cliente
import com.veterinaria.myapplication.data.Medicamento
import com.veterinaria.myapplication.data.Pedido
import com.veterinaria.myapplication.domain.CombinarPedido
import com.veterinaria.myapplication.domain.ValidationUtils
import com.veterinaria.myapplication.domain.annotations.Promocionable
import java.time.LocalDate
import kotlin.reflect.full.findAnnotations
/**
 * ViewModel que gestiona el flujo de Farmacia (Cliente y Pedido).
 * Sigue el patrón MVVM y sobrevive al ciclo de vida.
 */
class FarmaciaViewModel : ViewModel() {
	
	// --- DATOS INMUTABLES DEL DOMAIN ---
	// Lista de medicamentos disponibles, migrada de MedicamentoService.kt
	private val listaMedicamentos = listOf(
		Medicamento("Kittydoll", "analgesico", 10000), // Precio normal
		Medicamento("K-9", "vitamina", 5000),
		Medicamento("Matapiojo", "desparasitario", 2000)
	)
	
	// Almacena todos los pedidos realizados para la funcionalidad de combinación
	var pedidosRealizados by mutableStateOf(emptyList<Pedido>())
		private set
	
	
	// --- ESTADO DEL CLIENTE (PASO 1) ---
	
	var cliente: Cliente? by mutableStateOf(null)
		private set
	
	var nombreClienteInput by mutableStateOf("")
		private set
	var telefonoClienteInput by mutableStateOf("")
		private set
	var emailClienteInput by mutableStateOf("")
		private set
	
	var errorTelefono by mutableStateOf<String?>(null)
		private set
	var errorEmail by mutableStateOf<String?>(null)
		private set
	
	// Validaciones de Cliente
	val esClienteValido: Boolean
		get() = ValidationUtils.validarTelefono(telefonoClienteInput) &&
				ValidationUtils.validarEmail(emailClienteInput) &&
				telefonoClienteInput.isNotEmpty() &&
				emailClienteInput.isNotEmpty()
	
	
	// --- ESTADO DEL PEDIDO (PASO 2) ---
	
	// El objeto Pedido actual que se está construyendo
	var pedidoActual: Pedido? by mutableStateOf(null)
		private set
	
	// Estado para la selección del medicamento
	var medicamentoSeleccionado: Medicamento? by mutableStateOf(null)
		private set
	
	// Resultados de los cálculos (Reflection)
	var nombrePromocion by mutableStateOf<String?>(null)
		private set
	var descuentoAplicado by mutableStateOf(0.0)
		private set
	var precioFinal by mutableStateOf<Int?>(null)
		private set
	
	
	// --- MÉTODOS DE MANEJO DE CLIENTE ---
	fun onNombreClienteChange(newValue: String) {
		nombreClienteInput = newValue
	}
	
	fun onTelefonoClienteChange(newValue: String) {
		if (newValue.length <= 12 && newValue.all { it.isDigit() }) {
			telefonoClienteInput = newValue
			errorTelefono = if (newValue.isEmpty()) {
				"El teléfono es obligatorio."
			} else if (!ValidationUtils.validarTelefono(newValue)) {
				"El teléfono debe tener exactamente 12 dígitos (Ej: 569123456780)."
			} else { null }
		}
	}
	
	fun onEmailClienteChange(newValue: String) {
		emailClienteInput = newValue.trim()
		errorEmail = if (newValue.isEmpty()) {
			"El correo es obligatorio."
		} else if (!ValidationUtils.validarEmail(newValue)) {
			"El correo debe tener el formato correcto (ej: nombre@dominio.com)."
		} else { null }
	}
	
	fun guardarCliente(): Boolean {
		if (esClienteValido) {
			val nombre = nombreClienteInput.ifBlank { "Cliente desconocido" }
			val telefonoFormateado = ValidationUtils.formatearTelefono(telefonoClienteInput)
			
			cliente = Cliente(
				nombre = nombre,
				telefono = telefonoFormateado,
				email = emailClienteInput
			)
			return true
		}
		return false
	}
	
	// --- MÉTODOS DE MANEJO DE PEDIDO ---
	fun getMedicamentosDisponibles(): List<Medicamento> {
		return listaMedicamentos
	}
	
	/*
	 * Lógica clave: Selecciona el medicamento y aplica el descuento usando Reflection.
	 */
	fun onMedicamentoSelect(medicamento: Medicamento) {
		medicamentoSeleccionado = medicamento
		
		// 1. Obtener anotaciones de Promoción usando Reflection
		val promociones = medicamento::class.findAnnotations<Promocionable>()
		val promocionesDisponibles = promociones.filter { it.tipo.equals(medicamento.tipo, ignoreCase = true) }
		
		var precioCalculado = medicamento.precio
		var descuentoMaximo = 0.0
		
		if (promocionesDisponibles.isNotEmpty()) {
			val promocion = promocionesDisponibles.first()
			descuentoMaximo = promocion.descuento
			nombrePromocion = promocion.nombre
			
			// Aplicar descuento
			precioCalculado = (medicamento.precio - (medicamento.precio * descuentoMaximo)).toInt()
		} else {
			nombrePromocion = null
		}
		
		descuentoAplicado = descuentoMaximo
		precioFinal = precioCalculado
	}
	
	/*
	 * Finaliza el pedido y lo añade a la lista histórica.
	 */
	fun finalizarPedido(): Boolean {
		val clienteActual = cliente
		val medicamento = medicamentoSeleccionado
		val total = precioFinal
		
		if (clienteActual != null && medicamento != null && total != null) {
			// Lógica de rangos de fechas (Migrada de Veterinaria.kt, simplificada)
			val fechaInicioPromo = LocalDate.now().minusDays(1)
			val fechaFinPromo = fechaInicioPromo.plusDays(3)
			
			val nuevoPedido = Pedido(
				cliente = clienteActual,
				medicamento = medicamento,
				total = total,
				precioNormal = medicamento.precio,
				inicioPromocion = fechaInicioPromo,
				terminoPromocion = fechaFinPromo
			)
			
			// Almacenar el pedido actual
			pedidoActual = nuevoPedido
			
			// Añadir a la lista histórica de pedidos
			pedidosRealizados = pedidosRealizados + nuevoPedido
			
			return true
		}
		return false
	}
	
	/*
	 * Combina todos los pedidos realizados históricamente.
	 */
	fun combinarPedidos(): Pedido? {
		if (pedidosRealizados.size < 2) {
			return null
		}
		// Usa la función Domain PedidoCombinator.combinar() en lugar del operador '+' directo
		return pedidosRealizados.reduce { acc, p -> CombinarPedido.combinar(acc, p) }
	}
}