package com.veterinaria.myapplication.data

/*
 * Representa al Cliente de la farmacia.
 * Migrada de 'model/Cliente.kt'. Hereda de Usuario y contiene lógica de sobrecarga
 * y evaluación de igualdad.
 */
class Cliente(
	nombre: String,
	val telefono: String,
	val email: String
): Usuario(nombre) {
	
	// Sobrecarga del operador '+' (combinación de nombres para el pedido combinado)
	operator fun plus(other: Cliente): String = "${this.nombre} + ${other.nombre}"
	
	// Component functions para desestructuración (component1, component2, component3)
	operator fun component1(): String = this.nombre
	operator fun component2(): String = this.email
	operator fun component3(): String = this.telefono
	
	// Reglas de igualdad (dos clientes son iguales si nombre y email coinciden)
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Cliente) return false
		return this.nombre == other.nombre && this.email == other.email
	}
	
	override fun hashCode(): Int {
		var result = nombre.hashCode()
		result = 31 * result + email.hashCode()
		return result
	}
}