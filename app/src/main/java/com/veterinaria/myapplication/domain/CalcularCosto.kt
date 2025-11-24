package com.veterinaria.myapplication.domain

import com.veterinaria.myapplication.data.TipoConsulta

object CalcularCosto {
	/**
	 * Calcula el valor total de la consulta aplicando los descuentos definidos en TipoConsulta.
	 * @param tipo El tipo de consulta (PELUQUERIA, CONTROL, OTRO).
	 * @return El costo final de la consulta con el descuento aplicado (Double).
	 */
	fun calcularValorTotal(tipo: TipoConsulta): Double {
		val valorBase = tipo.valorBase
		val descuentoAplicado = valorBase * tipo.descuento
		return valorBase - descuentoAplicado
	}
}