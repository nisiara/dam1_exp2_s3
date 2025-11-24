package com.veterinaria.myapplication.domain

import com.veterinaria.myapplication.data.Veterinario
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object AsignarVeterinario {
	// Lista de veterinarios y sus turnos, migrada de ConsultaService.kt
	private val veterinariosDisponibles = listOf(
		Veterinario("Gabriel Chavez", listOf("lunes", "miércoles")),
		Veterinario("Humberto Velez", listOf("martes", "jueves")),
		Veterinario("Victor Delgado", listOf("viernes", "sábado"))
	)
	
	/*
	 * Asigna un veterinario basado en el día actual del sistema,
	 * utilizando la lógica de revisión de Veterinarios de tu proyecto.
	 */
	fun asignarVeterinarioHoy(): String {
		val hoy = LocalDate.now()
		// Convierte el nombre del día a minúsculas y español para coincidir con la lista de turnos
		val nombreDiaHoy = hoy.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).lowercase()
		
		val veterinariosDeTurno = veterinariosDisponibles.filter { veterinario ->
			veterinario.turno.contains(nombreDiaHoy)
		}
		
		// Retorna el nombre del primer veterinario disponible o un mensaje de no disponibilidad
		return veterinariosDeTurno.firstOrNull()?.nombre ?: "No hay veterianrios asignados para hoy."
	}
	
	// Horarios ocupados migrados de ConsultaService.kt para que la UI los pueda consultar
	val horasOcupadas = listOf("10:00", "11:30", "15:00")
	
	/*
	 * Verifica si una hora ya está ocupada.
	 */
	fun esHoraOcupada(hora: String): Boolean {
		return horasOcupadas.contains(hora)
	}
}