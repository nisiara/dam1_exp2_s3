package com.veterinaria.myapplication.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PlanificarVacuna {
	/*
	 * Calcula la fecha de la próxima vacuna basándose en la edad de la mascota.
	 * @param edadMascota Edad en años (Int).
	 * @return Fecha de la próxima vacuna en formato "dd-MM-yyyy".
	 */
	fun calcularProximaVacuna(edadMascota: Int): String {
		val fechaHoy = LocalDate.now()
		val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")
		
		val fechaProximaVacuna = when (edadMascota) {
			in 0..1 -> fechaHoy.plusMonths(6) // Semestral
			else -> fechaHoy.plusMonths(12) // Anual o general
		}
		
		return fechaProximaVacuna.format(formatoFecha)
	}
	
	/*
	 * Genera un mensaje sobre la necesidad de vacuna basado en la edad.
	 */
	fun getMensajeNecesidadVacuna(edadMascota: Int): String {
		return when (edadMascota) {
			in 0..1 -> "Necesita su vacuna semestral."
			in 2..6 -> "Necesita su vacuna anual."
			else -> "La vacuna para la mascota es opcional."
		}
	}
}