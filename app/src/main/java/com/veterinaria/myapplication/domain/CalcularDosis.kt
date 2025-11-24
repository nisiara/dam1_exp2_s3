package com.veterinaria.myapplication.domain

object CalcularDosis {
	/*
	 * Calcula la dosis de vacuna recomendada en mililitros (ml) basada en el peso y la edad.
	 * La lógica se adapta de la versión original (que usaba Int) para ser más precisa con Double.
	 * @param pesoMascota Peso en kg (Double).
	 * @param edadMascota Edad en años (Int).
	 * @return Dosis recomendada en ml (Double).
	 */
	fun calcularDosisRecomendada(pesoMascota: Double, edadMascota: Int): Double {
		var dosisBase = 5.0 // Se inicializa como Double
		
		// Lógica adaptada para trabajar con rangos y Doubles
		if (edadMascota in 0..3) {
			when {
				// Rangos adaptados para peso Double
				pesoMascota <= 1.0 -> dosisBase = 2.0
				pesoMascota > 1.0 && pesoMascota <= 2.0 -> dosisBase = 3.0
				pesoMascota > 2.0 && pesoMascota <= 3.0 -> dosisBase = 4.0
				else -> dosisBase
			}
		} else if (edadMascota in 4..6) {
			when {
				pesoMascota >= 3.0 && pesoMascota <= 4.0 -> dosisBase = 5.0
				pesoMascota > 4.0 && pesoMascota <= 6.0 -> dosisBase = 6.0
				else -> dosisBase *= 2.0
			}
		} else {
			dosisBase *= 3.0 // Mayor a 6 años
		}
		
		return dosisBase
	}
}