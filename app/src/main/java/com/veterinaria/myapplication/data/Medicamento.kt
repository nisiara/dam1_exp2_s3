package com.veterinaria.myapplication.data
import com.veterinaria.myapplication.domain.annotations.Promocionable
/*
 * Representa un Medicamento.
 * Migrada de 'model/Medicamento.kt'. Contiene anotaciones de promociones
 * y sobrecarga de operadores.
 */
@Promocionable(nombre = "10% Analgésico", tipo = "analgesico", descuento = 0.1)
@Promocionable(nombre = "50% Vitamina", tipo = "vitamina", descuento = 0.5)
open class Medicamento(
	val nombre: String,
	val tipo: String,
	val precio: Int,
) {
	
	// Component functions para desestructuración
	operator fun component1(): String = this.nombre
	operator fun component2(): String = this.tipo
	operator fun component3(): Int = this.precio
	
	// Sobrecarga del operador '+' (combinación de nombres)
	operator fun plus(other: Medicamento): String = "${this.nombre} + ${other.nombre}"
	
	// Reglas de igualdad (dos medicamentos son iguales si nombre y tipo coinciden)
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Medicamento) return false
		return this.nombre == other.nombre && this.tipo == other.tipo
	}
	
	override fun hashCode(): Int {
		var result = nombre.hashCode()
		result = 31 * result + tipo.hashCode()
		return result
	}
}